package com.anland.consumer;

import android.content.Context;
import android.hardware.input.InputManager;
import android.view.InputDevice;

/** Discovery for Settings. Input itself is read exclusively by the root helper. */
final class GoldKeyboard {
    // Keep in sync with Gold's fn_remap.c and input_grab.c.
    static final String GOLD_NAME_PREFIX = "Gold Keyboardremaps";
    static final int GOLD_VENDOR = 0xffff;
    static final int GOLD_PRODUCT = 0xffff;
    // This device's Gold module presents its virtual keyboard as a native
    // Xiaomi one (15d9:00a3) so MIUI enables its native shortcut handling.
    // That is still Gold's output; accept the identity alongside upstream's.
    static final int GOLD_VENDOR_XIAOMI = 0x15d9;
    static final int GOLD_PRODUCT_XIAOMI = 0x00a3;

    private GoldKeyboard() {}

    static boolean isGoldIdentity(int vendorId, int productId) {
        return (vendorId == GOLD_VENDOR && productId == GOLD_PRODUCT)
                || (vendorId == GOLD_VENDOR_XIAOMI && productId == GOLD_PRODUCT_XIAOMI);
    }

    static boolean matchesGoldKeyboard(boolean present, String name, int vendorId,
                                       int productId, int sources) {
        return present && name != null && name.startsWith(GOLD_NAME_PREFIX)
                && isGoldIdentity(vendorId, productId)
                && (sources & InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD;
    }

    static boolean goldKeyboardPresent(Context context) {
        InputManager manager = context.getSystemService(InputManager.class);
        if (manager == null)
            return false;
        for (int id : manager.getInputDeviceIds()) {
            InputDevice device = InputDevice.getDevice(id);
            if (device != null && matchesGoldKeyboard(true, device.getName(),
                    device.getVendorId(), device.getProductId(), device.getSources()))
                return true;
        }
        return false;
    }

    static boolean matches(InputDevice device) {
        return device != null && matchesGoldKeyboard(true, device.getName(),
                device.getVendorId(), device.getProductId(), device.getSources());
    }
}
