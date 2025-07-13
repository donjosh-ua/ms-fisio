package com.ms_fisio.subscription.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_fisio.shared.domain.enums.DurationUnit;
import com.ms_fisio.subscription.domain.dto.CreateSubscriptionRequest;
import com.ms_fisio.subscription.domain.model.PlanTypeModel;
import com.ms_fisio.subscription.domain.model.SubscriptionModel;
import com.ms_fisio.subscription.repository.PlanTypeRepository;
import com.ms_fisio.subscription.repository.SubscriptionRepository;
import com.ms_fisio.subscription.service.SubscriptionService;
import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanTypeRepository planTypeRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.secret}")
    private String secret;

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Override
    public ResponseEntity<String> createOrder(CreateSubscriptionRequest request) {
        log.info("Recibida solicitud de creación: {}", request);
        try {
            Optional<PlanTypeModel> planOpt = planTypeRepository.findById(request.getPlanTypeId());
            Optional<UserModel> userOpt = userRepository.findById(request.getUserId());

            if (planOpt.isEmpty() || userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario o plan inválido");
            }

            PlanTypeModel plan = planOpt.get();
            Double price = plan.getPrice();
            String accessToken = getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = String.format("""
                        {
                          "intent": "CAPTURE",
                          "purchase_units": [{
                            "amount": {
                              "currency_code": "USD",
                              "value": "%.2f"
                            }
                          }],
                          "application_context": {
                            "return_url": "http://localhost:8081/api/subscription/capture",
                            "cancel_url": "http://localhost:8081/api/subscription/cancel"
                          }
                        }
                    """, price);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl + "/v2/checkout/orders",
                    entity,
                    String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error en la respuesta de PayPal: {}", response.getBody());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la respuesta de PayPal");
            }

            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode linksNode = json.get("links");
            String approveLink = "";
            if (linksNode != null && linksNode.isArray()) {
                for (JsonNode link : linksNode) {
                    if (link.has("rel") && "approve".equals(link.get("rel").asText())) {
                        approveLink = link.get("href").asText();
                        break;
                    }
                }
            }
            if (approveLink.isEmpty()) {
                log.error("No se encontró el link de aprobación en la respuesta de PayPal: {}", response.getBody());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No se pudo obtener el link de aprobación de PayPal");
            }
            return ResponseEntity.ok(approveLink);

        } catch (Exception e) {
            log.error("Error creando orden PayPal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }
    }

    @Override
    public ResponseEntity<String> captureOrder(String orderId) {
        try {
            String accessToken = getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl + "/v2/checkout/orders/" + orderId + "/capture",
                    entity,
                    String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error en la respuesta de PayPal: {}", response.getBody());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la respuesta de PayPal");
            }

            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode payerNode = json.get("payer");
            if (payerNode == null || payerNode.get("email_address") == null) {
                log.error("No se encontró el email del pagador en la respuesta de PayPal: {}", response.getBody());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No se pudo obtener el email del pagador");
            }
            String payerEmail = payerNode.get("email_address").asText();

            Optional<UserModel> userOpt = userRepository.findByEmail(payerEmail);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no encontrado");
            }

            // Por defecto asignamos el primer plan
            PlanTypeModel plan = planTypeRepository.findAll().stream().findFirst().orElseThrow();

            SubscriptionModel subscription = new SubscriptionModel();
            subscription.setUser(userOpt.get());
            subscription.setPlanType(plan);
            subscription.setStartDate(LocalDate.now());
            subscription.setDurationValue(1); // Por defecto 1 mes
            subscription.setDurationUnit(DurationUnit.MONTH);

            subscriptionRepository.save(subscription);

            return ResponseEntity.ok("Subscripción activada correctamente");

        } catch (Exception e) {
            log.error("Error capturando orden PayPal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }
    }

    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, secret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/v1/oauth2/token",
                entity,
                String.class);
        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener el token de PayPal", e);
        }
    }
}