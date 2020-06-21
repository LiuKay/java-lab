package com.kay.concurrency.practice.computecahe;

/**
 *  设计一个缓存用来存储每次计算过的任务，Key 为任务的输入, Value 为任务的结果，
 *  计算一个任务时，首先应该去缓存里面查找，如果存在则返回，如果不存在则进行计算，假设计算是一个耗时任务。
 */
