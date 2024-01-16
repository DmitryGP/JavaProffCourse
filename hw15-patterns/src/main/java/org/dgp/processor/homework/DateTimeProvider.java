package org.dgp.processor.homework;

import java.time.LocalDateTime;

@FunctionalInterface
public interface DateTimeProvider {
    LocalDateTime getDate();
}
