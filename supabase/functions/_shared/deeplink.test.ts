import {
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  DEFAULT_SCHEME,
  sanitizeScheme,
  withSchemeParam,
} from "./deeplink.ts";

Deno.test("sanitizeScheme accepts only whitelisted schemes", () => {
  assertEquals(sanitizeScheme("needsvswants"), "needsvswants");
  assertEquals(sanitizeScheme("needsvswantsplain"), "needsvswantsplain");
  assertEquals(sanitizeScheme("javascript"), DEFAULT_SCHEME);
  assertEquals(sanitizeScheme("needsvswantsplain2"), DEFAULT_SCHEME);
  assertEquals(sanitizeScheme(""), DEFAULT_SCHEME);
  assertEquals(sanitizeScheme(undefined), DEFAULT_SCHEME);
  assertEquals(sanitizeScheme(42), DEFAULT_SCHEME);
});

Deno.test("withSchemeParam leaves the default scheme URL untouched", () => {
  assertEquals(
    withSchemeParam("https://x.test/paypal-return.html", "needsvswants"),
    "https://x.test/paypal-return.html",
  );
  assertEquals(
    withSchemeParam("https://x.test/paypal-return.html", "bogus"),
    "https://x.test/paypal-return.html",
  );
});

Deno.test("withSchemeParam appends with ? or & as needed", () => {
  assertEquals(
    withSchemeParam("https://x.test/r.html", "needsvswantsplain"),
    "https://x.test/r.html?scheme=needsvswantsplain",
  );
  assertEquals(
    withSchemeParam("https://x.test/r.html?a=1", "needsvswantsplain"),
    "https://x.test/r.html?a=1&scheme=needsvswantsplain",
  );
});
