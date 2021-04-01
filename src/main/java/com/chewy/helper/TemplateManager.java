package com.chewy.helper;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.Introspected;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


@Slf4j
@Introspected
public class TemplateManager {

  private final String PREFIX = "common";

  @Property(name = "email.active-bucket")
  private String activeTemplatesBucket;

  @Property(name = "email.local.template.dir")
  private String localTemplatesDir;

  private S3Client amazonS3;

  TemplateManager() {
    initS3Client();
  }

  public void downloadTemplates() {
    log.info(
        "downloading all active message templates from s3 bucket {} to local dir {}",
        activeTemplatesBucket,
        localTemplatesDir);
    for (S3Object s3Object : listObjects()) {
      boolean pathExists =
          Files.exists(Paths.get(localTemplatesDir + s3Object.key()), LinkOption.NOFOLLOW_LINKS);
      if (pathExists) continue;
      File tempFile = new File(localTemplatesDir, s3Object.key());
      amazonS3.getObject(
          GetObjectRequest.builder().bucket(activeTemplatesBucket).key(s3Object.key()).build(),
          ResponseTransformer.toFile(tempFile));
    }
    log.info("downloading templates completed");
  }

  public void downloadActiveTemplates() {
    log.info(
        "downloading all active message templates from nested folders from s3 bucket {} to local dir {}",
        activeTemplatesBucket,
        localTemplatesDir);

    for (S3Object s3Object : listPrefixObjects()) {
      if (s3Object.key().endsWith("/")) {
        Path path = Paths.get(localTemplatesDir + s3Object.key());
        try {
          Files.createDirectories(path);
        } catch (IOException e) {
          e.printStackTrace();
        }

      } else {
        boolean pathExists =
                Files.exists(Paths.get(localTemplatesDir + s3Object.key()), LinkOption.NOFOLLOW_LINKS);
        if (pathExists) continue;
        File tempFile = new File(localTemplatesDir + s3Object.key());
        amazonS3.getObject(
            GetObjectRequest.builder().bucket(activeTemplatesBucket).key(s3Object.key()).build(),
            ResponseTransformer.toFile(tempFile));
      }
    }
    //download main folder tempaltes
    downloadTemplates();
  }

  private List<S3Object> listObjects() {
    ListObjectsRequest listObjects =
        ListObjectsRequest.builder().bucket(activeTemplatesBucket).build();
    return amazonS3.listObjects(listObjects).contents();
  }

  private List<S3Object> listPrefixObjects() {
    ListObjectsRequest listObjects =
        ListObjectsRequest.builder().prefix(PREFIX).bucket(activeTemplatesBucket).build();
    return amazonS3.listObjects(listObjects).contents();
  }

  private void initS3Client() {
    amazonS3 = S3Client.builder().region(Region.US_EAST_1).build();
  }

  /**
   * Checks to see if there are any ftl files in the lambda localTemplatesDir
   *
   * @return true if an ftl file is in the localTemplatesDir
   */
  public boolean localTemplateDirectoryHasFtlFile() {
    for (String filename : Objects.requireNonNull(new File(localTemplatesDir).list())) {
      if (isFtlFile(filename)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks the extension of provided file to see if it is a freemarker file
   *
   * @param filename name of the file
   * @return If the filename provided is an .ftl file
   */
  private boolean isFtlFile(String filename) {
    String FREEMARKER_EXTENSION = "ftl";
    return filename.substring(filename.lastIndexOf('.') + 1).equals(FREEMARKER_EXTENSION);
  }

  private void purgeDirectory(String dir) throws IOException {
    Files.walk(Paths.get(dir)).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
  }
}
