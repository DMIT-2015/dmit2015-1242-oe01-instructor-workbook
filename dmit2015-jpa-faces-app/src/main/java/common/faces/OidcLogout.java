package common.faces;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.openid.OpenIdContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class OidcLogout {

    @Inject
    private HttpServletRequest request;

    @Inject
    private OpenIdContext _openIdContext;

    @Inject
    @ConfigProperty(name = "keycloak.oidc.providerURI")
    private String oidcProviderURI;

    @Inject
    @ConfigProperty(name = "keycloak.oidc.clientId")
    private String oidcClientId;

    @Inject
    @ConfigProperty(name = "keycloak.oidc.clientSecret")
    private String oidcClientSecret;

    public String submit() throws ServletException {
        /*
        final String logoutUrl = String.format("%s/protocol/openid-connect/logout", oidcProviderURI);
        Map<String, String> formData = new HashMap<>();
        formData.put("refresh_token", _openIdContext.getRefreshToken().orElseThrow().getToken());
        formData.put("client_id", oidcClientId);
        formData.put("client_secret", oidcClientSecret);

        String encodedFormData = formData.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        try (var httpClient = HttpClient.newHttpClient()) {
            var httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(logoutUrl))
                    .header("Content-Type","application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(encodedFormData, StandardCharsets.UTF_8))
                    .build();
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                Messages.addGlobalError("Logout failed with status %s", httpResponse.statusCode());
            }
        } catch (Exception e) {
            Messages.addGlobalError("Logout failed with exception %s", e.getMessage());
        }
         */

        final String logoutUrl = String.format("%s/protocol/openid-connect/logout", oidcProviderURI);
        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("refresh_token", _openIdContext.getRefreshToken().orElseThrow().getToken());
        formData.add("client_id", oidcClientId);
        formData.add("client_secret", oidcClientSecret);
        try (var restClient = ClientBuilder.newClient();
             Response httpResponse = restClient
                     .target(logoutUrl)
                     .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                     .post(Entity.form(formData))
        ) {
            if (httpResponse.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                Messages.addGlobalError("Logout failed with status %s", httpResponse.getStatus());
            }
        }

        request.getSession().invalidate();
        return "/index?faces-redirect=true";
    }
}