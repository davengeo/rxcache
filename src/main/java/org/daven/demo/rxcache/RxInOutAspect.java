/*
 * Copyright (c) 2015.
 * me@davengeo.com
 */

package org.daven.demo.rxcache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import java.util.EventListener;
import java.util.Optional;

@Aspect
@Component
public class RxInOutAspect {

    private static EmitListener emitListener;

    @SuppressWarnings("CodeBlock2Expr")
    private static ConnectableObservable<String> stream = Observable.create(
            (Subscriber<? super String> subscriber) -> {
                register(subscriber::onNext);
            }).subscribeOn(Schedulers.computation()).publish();

    private static void register(EmitListener listener) {
        emitListener = listener;
    }

    @Around("(anyRepositoryPointcut() && anyComponent() && anyPutExecution())")
    public Object emitAsObservable(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        Optional<Object[]> args = Optional.ofNullable(pjp.getArgs());
        if (args.isPresent()) {
            emitListener.emit(args.get()[0].toString());
        }
        return result;
    }

    @PostConstruct
    public void init() {
        stream.connect();
    }

    public Observable<String> getStream() {
        return stream;
    }

    //change the packaging
    @Pointcut("within(org.daven.demo.rxcache.*)")
    public void anyRepositoryPointcut() {
    }

    @Pointcut("execution(* put(..))")
    public void anyPutExecution() {

    }

    @Pointcut("@target(org.springframework.stereotype.Component)")
    public void anyComponent() {

    }


    private interface EmitListener extends EventListener {
        void emit(String str);
    }

}
