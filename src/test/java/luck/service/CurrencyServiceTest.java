package luck.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import luck.exception.LuckException;
import org.junit.jupiter.api.Test;

/** Tests currency command validation independently of the external API. */
class CurrencyServiceTest {
    private final CurrencyService service = new CurrencyService();

    @Test
    void invalidAmount_isRejected() {
        assertThrows(LuckException.class, () -> service.convert("abc USD to JPY"));
    }

    @Test
    void missingConversionSeparator_isRejected() {
        assertThrows(LuckException.class, () -> service.convert("100 USD JPY"));
    }

    @Test
    void nullInput_isRejected() {
        assertThrows(LuckException.class, () -> service.convert(null));
    }
}
