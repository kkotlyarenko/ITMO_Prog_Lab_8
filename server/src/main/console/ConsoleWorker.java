package main.console;

public abstract class ConsoleWorker {
    public abstract void print(String text);
    
    public abstract void error(String text);

    public void print(String pattern, Object... vals) {
        print(String.format(pattern, vals));
    }

    public abstract String get(String label);

    public void clear() {
        print("\n".repeat(30));
    }

    public void skip() {
        print("\n");
    }
}
