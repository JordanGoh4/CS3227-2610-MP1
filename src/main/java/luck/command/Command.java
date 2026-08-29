package luck.command;
import luck.exception.LuckException;
/** Defines executable command behavior. */
public interface Command {
    void execute(String arguments) throws LuckException;
}
