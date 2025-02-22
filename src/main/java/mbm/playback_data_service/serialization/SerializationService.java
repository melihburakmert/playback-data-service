package mbm.playback_data_service.serialization;

public interface SerializationService {
  <T> T deserialize(String message, Class<T> clazz);

  <T> String serialize(T message);
}
