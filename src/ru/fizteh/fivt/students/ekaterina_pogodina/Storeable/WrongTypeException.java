package ru.fizteh.fivt.students.ekaterina_pogodina.Storeable;

public class WrongTypeException extends RuntimeException {
    public WrongTypeException() {
        super();
    }
    public WrongTypeException(String message) {
        super(message);
    }
    public WrongTypeException(Throwable gotException) {
        super(gotException);
    }
    public WrongTypeException(String message, Throwable gotException) {
        super(message, gotException);
    }
}