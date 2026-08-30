package luck.command;

import luck.exception.LuckException;
import luck.service.CurrencyService;

/** Handles currency conversion requests. */
public class CurrencyCommand implements Command {
    private final CommandContext context;
    private final CurrencyService currencyService = new CurrencyService();

    /** Creates a currency command with the shared command context. */
    public CurrencyCommand(CommandContext context) {
        this.context = context;
    }

    /** Converts and displays the requested currency amount. */
    @Override
    public void execute(String arguments) throws LuckException {
        context.getUi().printMessage(currencyService.convert(arguments));
    }
}
