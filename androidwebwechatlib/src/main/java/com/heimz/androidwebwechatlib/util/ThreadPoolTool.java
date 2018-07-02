package com.heimz.androidwebwechatlib.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class ThreadPoolTool {

	private static final ThreadPoolExecutor executor;
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;



	static {
		executor = new ThreadPoolExecutor(CORE_POOL_SIZE , MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new ArrayBlockingQueue(128), new ThreadPoolExecutor.DiscardOldestPolicy());
	}

	public static final ThreadPoolExecutor getInstance() {
		return executor;
	}

}
