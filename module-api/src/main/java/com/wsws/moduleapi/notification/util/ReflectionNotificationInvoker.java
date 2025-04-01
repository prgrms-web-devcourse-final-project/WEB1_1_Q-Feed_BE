package com.wsws.moduleapi.notification.util;

import java.lang.reflect.Method;

// FcmType을 직접 접근x 테스트 시에만 접근 ( 리플렉션으로 넘김 : 유틸 클래스 사용)
public class ReflectionNotificationInvoker {

    private static final String FCM_TYPE_CLASS = "com.wsws.moduleexternalapi.fcm.util.FcmType";

    public static void invokeSendNotification(
            Object service,
            String senderId,
            String recipientId,
            String url,
            String fcmTypeName
    ) throws Exception {

        Class<Enum> fcmTypeClass = (Class<Enum>) Class.forName(FCM_TYPE_CLASS);
        Enum<?> enumInstance = Enum.valueOf(fcmTypeClass, fcmTypeName);

        Method method = service.getClass()
                .getMethod("sendNotification",
                        String.class, String.class, Long.class, Long.class, Long.class, String.class, fcmTypeClass);

        method.invoke(service,
                senderId,
                recipientId,
                1L,
                null,
                null,
                url,
                enumInstance
        );
    }
}
