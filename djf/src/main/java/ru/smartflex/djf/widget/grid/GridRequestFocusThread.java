package ru.smartflex.djf.widget.grid;

import ru.smartflex.djf.controller.WidgetManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * В потоке выставляем фокус на гриде трижды, иначе выставление фокусов на input перевешивает и перволначальный фокус с грида соскакивает
 * (свинговый костыль)
 */
public class GridRequestFocusThread {

    private static ExecutorService service = Executors.newSingleThreadExecutor();

    public static void requestFocusInThread(WidgetManager wm, SFGrid grid) {
        // executor нужен чтоюы упорядочить swing events, в противном случае они идут вперемешку а то и задом наперед
        service.submit(new GridFocuRequest(wm, grid));
    }

    static class GridFocuRequest implements Runnable {
        private WidgetManager wm;
        private SFGrid grid;

        public GridFocuRequest(WidgetManager wm, SFGrid grid) {
            this.wm = wm;
            this.grid = grid;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(10);
                grid.requestGridFocus();
                Thread.sleep(10);
                grid.requestGridFocus();
                Thread.sleep(10);
                grid.requestGridFocus();
                grid.markGridAsActive();

                wm.setAllowFocusMovement(true);
            } catch (Exception e) {
            }
        }
    }
}
