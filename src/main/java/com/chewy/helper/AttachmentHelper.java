package com.chewy.helper;

import com.chewy.HtmlPreviewThreadLocal;
import com.chewy.exception.NPCErrorCode;
import com.chewy.exception.NPCException;
import com.google.common.collect.ImmutableSet;
import io.micronaut.core.annotation.Introspected;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
@Slf4j
@Introspected
public class AttachmentHelper {

  private static final String TMP_ATTACHMENTS = "/tmp/attachments";

  /**
   * Delete created files after sending emails
   *
   * @param attachmentPath directory path to files to be deleted
   */
  public static void cleanupAttachmentDirectories(Set<String> attachmentPath) {
    if (attachmentPath == null || attachmentPath.isEmpty()) {
      return;
    }
    for (String s : attachmentPath) {
      File currentFile = new File(s);
      currentFile.delete();
    }
  }

  public static String buildAttachmentPathSeparateDirectories(String attachmentTemplateName, int directNum) {
    String attachmentPath = TMP_ATTACHMENTS +
        "/" +
        HtmlPreviewThreadLocal.get().getRid() +
        "/" +
        directNum +
        "/" +
        attachmentTemplateName;
    attachmentPath = attachmentPath.replaceAll("^\"|\"$", "");
    return attachmentPath;
  }

  public static String buildAttachmentPath(String attachmentTemplateName) {
    String attachmentPath = TMP_ATTACHMENTS +
        "/" +
        HtmlPreviewThreadLocal.get().getRid() +
        "/" +
        attachmentTemplateName;
    attachmentPath = attachmentPath.replaceAll("^\"|\"$", "");
    return attachmentPath;
  }

  public static void createAttachmentParentDirectory() throws NPCException {
    File tempFile = new File(TMP_ATTACHMENTS);
    if (tempFile.exists()) {
      log.info("parent attachment directory already exists");
    } else if (!tempFile.mkdir()) {
      throw new NPCException(NPCErrorCode.UNKNOWN, "unable to create attachments parent directory");
    }
  }

  public Set<String> createReturnAttachment(List<String> returnLabels) throws NPCException {
    if (returnLabels == null || returnLabels.isEmpty()) {
      return ImmutableSet.of();
    }

    String returnLabelPath = null;
    Set<String> attachmentPaths = new HashSet<>();
    try {
      createAttachmentParentDirectory();
      for (int i = 0; i < returnLabels.size(); i++) {
//        byte[] returnLabelData = ReturnsHelper.decryptReturnLabel(returnLabels.get(i));
        returnLabelPath = buildAttachmentPathSeparateDirectories("RETURN" + i + ".pdf", i);
        log.info("processing return_label_path={}", returnLabelPath);
        File outputFile = new File(returnLabelPath);
        if (outputFile.exists()) {
          log.info("return file directory already exists");
        } else if (!outputFile.getParentFile().mkdirs()) {
          throw new NPCException(NPCErrorCode.UNKNOWN, "unable to create return attachments child file directory");
        }

        outputFile.createNewFile();
//        Files.write(Paths.get(outputFile.getPath()), returnLabelData);
        attachmentPaths.add(returnLabelPath);
      }

    } catch (IOException e) {
      throw new NPCException(NPCErrorCode.INVALID_EVENT, "exception creating attachment for return_labels="
          + returnLabels, e);
    }

    log.info("saved return attachments in the following paths: {}", attachmentPaths);
    return attachmentPaths;
  }
}
