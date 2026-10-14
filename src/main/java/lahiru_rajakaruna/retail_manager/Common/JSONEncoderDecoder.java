package lahiru_rajakaruna.retail_manager.Common;

public interface JSONEncoderDecoder {
    public String encode(String payload);

    public String decode(String jwt);
}
