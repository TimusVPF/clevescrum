package me.clevecord.scrum.helpers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ResponseData<T> {

    public static final Map<String, Object> EMPTY_METADATA = Collections.emptyMap();

    private final T data;
    private final Map<String, Object> metadata;
}
