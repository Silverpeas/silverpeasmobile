package org.silverpeas.mobile.shared.services.rest;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.silverpeas.mobile.client.common.network.rest.RestCallback;
import org.silverpeas.mobile.client.common.network.rest.RestMethod;
import org.silverpeas.mobile.shared.dto.authentication.UserProfileDTO;

import static org.junit.jupiter.api.Assertions.*;

/** Checks the mobile/Core request contract without making browser HTTP requests. */
class ServiceAuthenticationTest {
  private final RecordingAuthentication service = new RecordingAuthentication();
  private final RestCallback<UserProfileDTO> callback = new RestCallback<>() {
    @Override
    public void onFailure(RestMethod method, Throwable error) {}
    @Override
    public void onSuccess(RestMethod method, UserProfileDTO profile) {}
  };

  @Test
  void passwordAuthenticationUsesBasicCredentialsRatherThanAStoredSession() {
    service.authentication(callback);
    assertRequest("", null);
    assertSame(callback, service.callback);
  }

  @Test
  void totpKeepsLeadingZeroesAndSendsTrustPreferenceInJsonOnly() {
    service.authenticateTwoFactor("012345", true, callback);
    assertRequest("/two-factor", "{\"code\":\"012345\",\"trustDevice\":true}");
    assertSame(callback, service.callback);
  }

  @Test
  void recoveryCodeUsesTheSameChallengeEndpointWithoutNumericConversion() {
    service.authenticateTwoFactor("aB12-cD34-ef56", false, callback);
    assertRequest("/two-factor", "{\"code\":\"aB12-cD34-ef56\",\"trustDevice\":false}");
  }

  @Test
  void codeCannotInjectAdditionalJsonFields() {
    service.authenticateTwoFactor("a\"\\\n\r\tb", false, callback);
    assertRequest("/two-factor", "{\"code\":\"a\\\"\\\\\\n\\r\\tb\",\"trustDevice\":false}");
  }

  @Test
  void trustedDeviceUsesTheCookieEndpointWithoutAnExplicitToken() {
    service.authenticateTrustedDevice(callback);
    assertRequest("/trusted-device", null);
    assertSame(callback, service.callback);
  }

  @Test
  void enrollmentStartsWithoutSendingAnArbitraryUserId() {
    service.startEnrollment(null);
    assertRequest("/enrollment", null);
  }

  @Test
  void enrollmentConfirmationSendsTheCodeAndTrustPreferenceInJsonOnly() {
    for (boolean trust : new boolean[] {false, true}) {
      service.confirmEnrollment("001234", trust, null);
      assertRequest("/enrollment/confirm",
          "{\"code\":\"001234\",\"trustDevice\":" + trust + "}");
    }
  }

  private void assertRequest(String path, String body) {
    assertEquals("POST", service.method);
    assertEquals("/silverpeas/services/authentication" + path, service.url);
    assertEquals(body, service.body);
    assertFalse(service.useSession, "A stale session must not replace Basic credentials");
  }

  private static class RecordingAuthentication extends ServiceAuthentication {
    String method;
    String url;
    Object body;
    boolean useSession;
    RestCallback<?> callback;

    @Override
    protected <T> void requestJson(String method, String url, Object body,
        Function<Object, T> mapper, RestCallback<T> callback, boolean useSession) {
      this.method = method;
      this.url = url;
      this.body = body;
      this.callback = callback;
      this.useSession = useSession;
    }
  }
}
