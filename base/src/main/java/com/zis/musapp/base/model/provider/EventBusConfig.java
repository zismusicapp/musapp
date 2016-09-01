package com.zis.musapp.base.model.provider;

import com.google.auto.value.AutoValue;

/**
 * Created by Zis{github.com/Zis} on 5/12/16.
 */
@AutoValue
public abstract class EventBusConfig {
  public static Builder builder() {
    return new AutoValue_EventBusConfig.Builder();
  }

  public abstract boolean logNoSubscriberMessages();

  public abstract boolean sendNoSubscriberEvent();

  public abstract boolean eventInheritance();

  public abstract boolean throwSubscriberException();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder logNoSubscriberMessages(final boolean logNoSubscriberMessages);

    public abstract Builder sendNoSubscriberEvent(final boolean sendNoSubscriberEvent);

    public abstract Builder eventInheritance(final boolean eventInheritance);

    public abstract Builder throwSubscriberException(
        final boolean throwSubscriberException);

    public abstract EventBusConfig build();
  }
}
