package com.project.test.testtask

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL

/*
DTO для получения картинок
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class ResultsDTO(@JsonProperty("results")var results: List<ResultDTO>) {
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class ResultDTO(@JsonProperty("urls")var urls: UrlsDTO) {
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class UrlsDTO(@JsonProperty("raw")var raw: URL,
                   @JsonProperty("full")var full: URL,
                   @JsonProperty("regular")var regular: URL,
                   @JsonProperty("small")var small: URL,
                   @JsonProperty("thumb")var thumb: URL) {
}