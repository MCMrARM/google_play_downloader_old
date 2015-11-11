package com.github.mcmrarm.playdl;

import com.github.mcmrarm.playdl.proto.GooglePlay;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class CheckinManager {

    private LoginManager myLogin;

    static public GooglePlay.DeviceConfigurationProto getDeviceConfig() {
        return GooglePlay.DeviceConfigurationProto.newBuilder()
                .setTouchScreen(3)
                .setKeyboard(1)
                .setNavigation(1)
                .setScreenLayout(2)
                .setHasHardKeyboard(false)
                .setHasFiveWayNavigation(false)
                .setScreenDensity(320)
                .setGlEsVersion(131072)
                .addAllSystemSharedLibrary(Arrays.asList(
                        "android.test.runner",
                        "com.android.future.usb.accessory",
                        "com.android.location.provider",
                        "com.android.nfc_extras",
                        "com.google.android.maps",
                        "com.google.android.media.effects",
                        "com.google.widevine.software.drm",
                        "javax.obex"))
                .addAllSystemAvailableFeature(Arrays.asList(
                        "android.hardware.audio.low_latency",
                        "android.hardware.bluetooth",
                        "android.hardware.bluetooth_le",
                        "android.hardware.camera",
                        "android.hardware.camera.autofocus",
                        "android.hardware.camera.flash",
                        "android.hardware.camera.front",
                        "android.hardware.camera.any",
                        "android.hardware.camera.external",
                        "android.hardware.camera.level.full",
                        "android.hardware.camera.level.full",
                        "android.hardware.camera.capability.manual_sensor",
                        "android.hardware.camera.capability.manual_post_processing",
                        "android.hardware.camera.capability.raw",
                        "android.hardware.consumerir",
                        "android.hardware.location",
                        "android.hardware.location.network",
                        "android.hardware.location.gps",
                        "android.hardware.microphone",
                        "android.hardware.nfc",
                        "android.hardware.nfc.hce",
                        "android.hardware.sensor.accelerometer",
                        "android.hardware.sensor.barometer",
                        "android.hardware.sensor.compass",
                        "android.hardware.sensor.gyroscope",
                        "android.hardware.sensor.light",
                        "android.hardware.sensor.proximity",
                        "android.hardware.sensor.stepcounter",
                        "android.hardware.sensor.stepdetector",
                        "android.hardware.screen.landscape",
                        "android.hardware.screen.portrait",
                        "android.hardware.telephony",
                        "android.hardware.telephony.cdma",
                        "android.hardware.telephony.gsm",
                        "android.hardware.faketouch",
                        "android.hardware.faketouch.multitouch.distinct",
                        "android.hardware.faketouch.multitouch.jazzhand",
                        "android.hardware.touchscreen",
                        "android.hardware.touchscreen.multitouch",
                        "android.hardware.touchscreen.multitouch.distinct",
                        "android.hardware.touchscreen.multitouch.jazzhand",
                        "android.hardware.usb.host",
                        "android.hardware.usb.accessory",
                        "android.hardware.wifi",
                        "android.hardware.wifi.direct"))
                .addAllNativePlatform((DeviceInfo.isX86 ?
                        Arrays.asList("x86_64", "x86") :
                        Arrays.asList("armeabi-v7a", "armeabi")))
                .setScreenWidth(1080)
                .setScreenHeight(1920)
                .addAllSystemSupportedLocale(Arrays.asList(
                        "af", "af_ZA", "am", "am_ET", "ar", "ar_EG", "bg", "bg_BG",
                        "ca", "ca_ES", "cs", "cs_CZ", "da", "da_DK", "de", "de_AT",
                        "de_CH", "de_DE", "de_LI", "el", "el_GR", "en", "en_AU",
                        "en_CA", "en_GB", "en_NZ", "en_SG", "en_US", "es", "es_ES",
                        "es_US", "fa", "fa_IR", "fi", "fi_FI", "fr", "fr_BE",
                        "fr_CA", "fr_CH", "fr_FR", "hi", "hi_IN", "hr", "hr_HR",
                        "hu", "hu_HU", "in", "in_ID", "it", "it_CH", "it_IT", "iw",
                        "iw_IL", "ja", "ja_JP", "ko", "ko_KR", "lt", "lt_LT", "lv",
                        "lv_LV", "ms", "ms_MY", "nb", "nb_NO", "nl", "nl_BE",
                        "nl_NL", "pl", "pl_PL", "pt", "pt_BR", "pt_PT", "rm",
                        "rm_CH", "ro", "ro_RO", "ru", "ru_RU", "sk", "sk_SK", "sl",
                        "sl_SI", "sr", "sr_RS", "sv", "sv_SE", "sw", "sw_TZ", "th",
                        "th_TH", "tl", "tl_PH", "tr", "tr_TR", "ug", "ug_CN", "uk",
                        "uk_UA", "vi", "vi_VN", "zh_CN", "zh_TW", "zu", "zu_ZA"))
                .addAllGlExtension(Arrays.asList(
                        "GL_ANDROID_extension_pack_es31a",
                        "GL_EXT_base_instance",
                        "GL_EXT_blend_minmax",
                        "GL_EXT_buffer_storage",
                        "GL_EXT_color_buffer_float",
                        "GL_EXT_color_buffer_half_float",
                        "GL_EXT_copy_image",
                        "GL_EXT_debug_label",
                        "GL_EXT_debug_marker",
                        "GL_EXT_discard_framebuffer",
                        "GL_EXT_disjoint_timer_query",
                        "GL_EXT_draw_buffers_indexed",
                        "GL_EXT_draw_elements_base_vertex",
                        "GL_EXT_frag_depth",
                        "GL_EXT_geometry_point_size",
                        "GL_EXT_geometry_shader",
                        "GL_EXT_gpu_shader5",
                        "GL_EXT_map_buffer_range",
                        "GL_EXT_multi_draw_indirect",
                        "GL_EXT_occlusion_query_boolean",
                        "GL_EXT_post_depth_coverage",
                        "GL_EXT_primitive_bounding_box",
                        "GL_EXT_raster_multisample",
                        "GL_EXT_render_snorm",
                        "GL_EXT_robustness",
                        "GL_EXT_separate_shader_objects",
                        "GL_EXT_shader_implicit_conversions",
                        "GL_EXT_shader_integer_mix",
                        "GL_EXT_shader_io_blocks",
                        "GL_EXT_shader_texture_lod",
                        "GL_EXT_shadow_samplers",
                        "GL_EXT_sRGB",
                        "GL_EXT_sRGB_write_control",
                        "GL_EXT_tessellation_point_size",
                        "GL_EXT_tessellation_shader",
                        "GL_EXT_texture_border_clamp",
                        "GL_EXT_texture_buffer",
                        "GL_EXT_texture_compression_dxt1",
                        "GL_EXT_texture_compression_s3tc",
                        "GL_EXT_texture_cube_map_array",
                        "GL_EXT_texture_filter_anisotropic",
                        "GL_EXT_texture_filter_minmax",
                        "GL_EXT_texture_format_BGRA8888",
                        "GL_EXT_texture_norm16",
                        "GL_EXT_texture_rg",
                        "GL_EXT_texture_sRGB_decode",
                        "GL_EXT_texture_storage",
                        "GL_EXT_texture_view",
                        "GL_EXT_unpack_subimage",
                        "GL_KHR_blend_equation_advanced",
                        "GL_KHR_blend_equation_advanced_coherent",
                        "GL_KHR_context_flush_control",
                        "GL_KHR_debug",
                        "GL_KHR_robust_buffer_access_behavior",
                        "GL_KHR_robustness",
                        "GL_KHR_texture_compression_astc_ldr",
                        "GL_OES_compressed_ETC1_RGB8_texture",
                        "GL_OES_copy_image",
                        "GL_OES_depth_texture",
                        "GL_OES_depth_texture_cube_map",
                        "GL_OES_depth24",
                        "GL_OES_depth32",
                        "GL_OES_draw_buffers_indexed",
                        "GL_OES_draw_elements_base_vertex",
                        "GL_OES_EGL_image",
                        "GL_OES_EGL_image_external",
                        "GL_OES_EGL_sync",
                        "GL_OES_element_index_uint",
                        "GL_OES_fbo_render_mipmap",
                        "GL_OES_geometry_point_size",
                        "GL_OES_geometry_shader",
                        "GL_OES_get_program_binary",
                        "GL_OES_gpu_shader5",
                        "GL_OES_mapbuffer",
                        "GL_OES_packed_depth_stencil",
                        "GL_OES_primitive_bounding_box",
                        "GL_OES_rgb8_rgba8",
                        "GL_OES_sample_shading",
                        "GL_OES_sample_variables",
                        "GL_OES_shader_image_atomic",
                        "GL_OES_shader_io_blocks",
                        "GL_OES_shader_multisample_interpolation",
                        "GL_OES_standard_derivatives",
                        "GL_OES_surfaceless_context",
                        "GL_OES_tessellation_point_size",
                        "GL_OES_tessellation_shader",
                        "GL_OES_texture_border_clamp",
                        "GL_OES_texture_buffer",
                        "GL_OES_texture_cube_map_array",
                        "GL_OES_texture_float",
                        "GL_OES_texture_float_linear",
                        "GL_OES_texture_half_float",
                        "GL_OES_texture_half_float_linear",
                        "GL_OES_texture_npot",
                        "GL_OES_texture_stencil8",
                        "GL_OES_texture_storage_multisample_2d_array",
                        "GL_OES_texture_view",
                        "GL_OES_vertex_array_object",
                        "GL_OES_vertex_half_float")).build();
    }

    private byte[] getRequest() {
        return GooglePlay.AndroidCheckinRequest.newBuilder()
                .setId(0)
                .setCheckin(GooglePlay.AndroidCheckinProto.newBuilder().
                        setBuild(GooglePlay.AndroidBuildProto.newBuilder()
                            .setId("google/yakju/maguro:4.1.1/JRO03C/398337:user/release-keys")
                            .setProduct(DeviceInfo.PRODUCT)
                            .setCarrier(DeviceInfo.CARRIER)
                            .setBootloader(DeviceInfo.BOOTLOADER)
                            .setClient("android-google")
                            .setTimestamp(new Date().getTime() / 1000)
                            .setDevice(DeviceInfo.DEVICE)
                            .setSdkVersion(DeviceInfo.SDK_VERSION)
                            .setGoogleServices(DeviceInfo.GOOGLE_SERVICES_VERSION)
                            .setModel(DeviceInfo.MODEL)
                            .setManufacturer(DeviceInfo.MANUFACTURER)
                            .setBuildProduct(DeviceInfo.BUILD_PRODUCT)
                            .setOtaInstalled(false))
                        .setLastCheckinMsec(0)
                        .addEvent(GooglePlay.AndroidEventProto.newBuilder()
                                .setTag("event_log_start")
                                .setTimeMsec(new Date().getTime()))
                        .setUserNumber(0)).setLocale(DeviceInfo.lang)
                .setLoggingId((new Random().nextLong()))
                .addAccountCookie("[" + myLogin.getEmail() + "]")
                .addAccountCookie(myLogin.getAuth())
                .setTimeZone("America/New_York")
                .setVersion(3)
                .setDeviceConfiguration(getDeviceConfig())
                .setFragment(0)
                .build()
                .toByteArray();
    }

    String androidId;

    private void execRequest() throws IOException {
        if (androidId != null)
            return;

        HttpPost request = new HttpPost("https://android.clients.google.com/checkin");
        request.setHeader("Content-type", "application/x-protobuffer");
        request.setHeader("Content-Encoding", "gzip");
        request.setHeader("Accept-Encoding", "gzip");
        request.setHeader("User-Agent", "Android-Checkin/2.0 (maguro JRO03L); gzip");

        request.setEntity(new ByteArrayEntity(getRequest()));

        try {
            HttpResponse response = new HttpClientGzip().execute(request);
            HttpEntity entity = response.getEntity();
            if (entity == null)
                throw new IOException(response.getStatusLine().toString());

            InputStream inStream = entity.getContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int read;
            while ((read = inStream.read(buffer, 0, buffer.length)) != -1)
                baos.write(buffer, 0, read);
            baos.flush();
            GooglePlay.AndroidCheckinResponse parsed_response = GooglePlay.AndroidCheckinResponse.parseFrom(baos.toByteArray());

            long aid = parsed_response.getAndroidId();
            if (aid == 0)
                throw new IOException("Can't find android_id" + " // " + response.getStatusLine().toString());

            androidId = Long.toString(aid, 16);
        } finally {
            request.releaseConnection();
        }
    }

    void login(String email, String password) throws IOException, LoginManager.LoginException {
        myLogin = new LoginManager("ac2dm", "com.google.android.gsf");
        myLogin.setEmail(email);
        myLogin.login(password, null);
    }

    String getAndroidId() throws IOException {
        execRequest();
        return androidId;
    }

}
