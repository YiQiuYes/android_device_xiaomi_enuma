#
# Copyright (C) 2023 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit some common Lineage stuff.
$(call inherit-product, vendor/lineage/config/common_full_tablet.mk)

PRODUCT_CHARACTERISTICS := tablet

# Inherit from enuma device
$(call inherit-product, device/xiaomi/enuma/device.mk)

PRODUCT_NAME := lineage_enuma
PRODUCT_DEVICE := enuma
PRODUCT_MANUFACTURER := Xiaomi
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := M2105K81AC

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi

BUILD_FINGERPRINT := Xiaomi/enuma/enuma:13/RKQ1.211001.001/V816.0.3.0.TKZCNXM:user/release-keys
