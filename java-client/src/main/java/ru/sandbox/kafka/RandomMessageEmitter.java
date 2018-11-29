package ru.sandbox.kafka;
import com.google.gson.Gson;
import io.reactivex.*;
import java.util.concurrent.TimeUnit;

import ru.sandbox.generator.IGeneratedMessage;
import ru.sandbox.generator.MessageGenerator;


public class RandomMessageEmitter {
    private Observable<IGeneratedMessage> emitter;
    public static void main(String[] args) {
        RandomMessageEmitter messageEmitter = new RandomMessageEmitter();
        messageEmitter.run();
    }

    public RandomMessageEmitter() {
        this.emitter = Observable.interval(100, TimeUnit.MILLISECONDS).concatMap(x -> {
            return Observable.just(x)
                    .delay((long)(Math.random() * 2900), TimeUnit.MILLISECONDS)
                    .timeInterval().map(tick -> {
                        return new MessageGenerator().generateMessage();
                    });
        });
    }

    public void run() {
        // this.emitter.subscribeOn
        // this.emitter.observeOn
        this.emitter.blockingSubscribe(o -> {
            System.out.println(new Gson().toJson(o));
        });
    }
}
