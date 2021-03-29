//package example.helper;
//
//import com.chewy.returns.model.event.ReturnEvent;
//import io.micronaut.core.annotation.Introspected;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.inject.Singleton;
//import java.io.IOException;
//
//import static com.chewy.domain.MessageTypeName.RETURN_LABELS_GENERATED_DROPSHIP;
//
//@Slf4j
//@Singleton
//@Introspected
//public class ReturnsHelper {
//
//  public static byte[] decryptReturnLabel(String encryptedReturnLabel) throws IOException {
//    return EventUtil.getDecodedUncompressedBytes(encryptedReturnLabel, true);
//  }
//  public static boolean eventReturnLabelEligible(String eventType) {
//    return ReturnEvent.Type.RETURN_LABELS_GENERATED.name().equals(eventType)
//        || RETURN_LABELS_GENERATED_DROPSHIP.equals(eventType);
//  }
//}
