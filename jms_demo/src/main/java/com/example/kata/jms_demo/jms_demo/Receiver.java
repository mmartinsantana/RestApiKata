package com.example.kata.jms_demo.jms_demo;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) {
    System.out.println("Received <" + message + ">");

    if (message.equals("plof")) {
      throw new RuntimeException("plof");
    }

    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}