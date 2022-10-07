/*
 * Ski Data API for NEU Seattle distributed systems course
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * OpenAPI spec version: 2.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
/**
 * ResortSkiers
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-10-03T22:01:03.053Z[GMT]")
public class ResortSkiers {
  @SerializedName("time")
  private String time = null;

  @SerializedName("numSkiers")
  private Integer numSkiers = null;

  public ResortSkiers time(String time) {
    this.time = time;
    return this;
  }

   /**
   * Get time
   * @return time
  **/
  @Schema(example = "Mission Ridge", description = "")
  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public ResortSkiers numSkiers(Integer numSkiers) {
    this.numSkiers = numSkiers;
    return this;
  }

   /**
   * Get numSkiers
   * @return numSkiers
  **/
  @Schema(example = "78999", description = "")
  public Integer getNumSkiers() {
    return numSkiers;
  }

  public void setNumSkiers(Integer numSkiers) {
    this.numSkiers = numSkiers;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResortSkiers resortSkiers = (ResortSkiers) o;
    return Objects.equals(this.time, resortSkiers.time) &&
        Objects.equals(this.numSkiers, resortSkiers.numSkiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, numSkiers);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResortSkiers {\n");
    
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    numSkiers: ").append(toIndentedString(numSkiers)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
