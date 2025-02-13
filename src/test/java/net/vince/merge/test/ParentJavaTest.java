package net.vince.merge.test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.vavr.jackson.datatype.VavrModule;
import io.vavr.jackson.datatype.VavrModule.Settings;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ParentJavaTest {

  private String loadJson(String path) throws IOException {
    return new String(getClass().getResourceAsStream(path).readAllBytes());
  }

  @Test
  void test() throws Exception {
    //Given
    var mapper = new ObjectMapper()
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setDefaultPropertyInclusion(Include.NON_EMPTY)
        .registerModule(new VavrModule(new Settings().deserializeNullAsEmptyCollection(true)));

    var parent = loadJson("/parent.json");
    var toMerge = loadJson("/to_merge.json");
    var expected = loadJson("/result.json");

    //When
    var parentObject = mapper.readerFor(ParentJava.class).readValue(parent);
    var updated = mapper.readerForUpdating(parentObject).readValue(toMerge);
    var expectedObject = mapper.readerFor(ParentJava.class).readValue(expected);

    //Then
    assertThat(updated).isEqualTo(expectedObject);
  }
}
