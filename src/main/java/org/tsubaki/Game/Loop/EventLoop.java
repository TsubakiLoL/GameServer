package org.tsubaki.Game.Loop;

public abstract class EventLoop {
    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    private final int FPS;
    private final long targetFrameTimeNanos;
    private volatile boolean running;
    private Thread loopThread;

    public EventLoop(int fps) {
        this.FPS = fps;
        this.targetFrameTimeNanos = NANOS_PER_SECOND / fps;
    }

    abstract void Process(float delta);
    abstract void Ready();

    public synchronized void Start() {
        if (running) return;
        running = true;
        loopThread = new Thread(this::runLoop, "EventLoop-Thread");
        loopThread.start();
    }

    public synchronized void Stop() {
        running = false;
        if (loopThread != null) {
            try {
                loopThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            loopThread = null;
        }
    }

    private void runLoop() {
        Ready();

        long lastTime = System.nanoTime();
        long timer = System.nanoTime();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            long elapsedNanos = now - lastTime;
            lastTime = now;

            // 计算帧时间（秒）
            float delta = elapsedNanos / (float)NANOS_PER_SECOND;

            // 处理业务逻辑
            Process(delta);
            frames++;

            // 性能统计（每秒输出FPS）
            if (System.nanoTime() - timer >= NANOS_PER_SECOND) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += NANOS_PER_SECOND;
            }

            // 帧率控制
            long frameEndTime = System.nanoTime();
            long sleepTime = targetFrameTimeNanos - (frameEndTime - now);

            if (sleepTime > 0) {
                try {
                    long millis = sleepTime / 1_000_000;
                    int nanos = (int)(sleepTime % 1_000_000);
                    Thread.sleep(millis, nanos);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            }
        }
    }
}