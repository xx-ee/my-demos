package xxd.demos.mmap.store;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiedong
 * Date: 2024/2/22 17:20
 */
@Slf4j
public class MappedFile {

    //JVM中映射的虚拟内存总大小
    private static final AtomicLong TOTAL_MAPPED_VIRTUAL_MEMORY = new AtomicLong(0);
    //JVM中mmap的数量
    private static final AtomicInteger TOTAL_MAPPED_FILES = new AtomicInteger(0);

    //映射文件的大小
    protected int fileSize;
    //映射文件的大小
    protected FileChannel fileChannel;
    //映射的文件名
    private String fileName;
    //映射的文件
    private File file;
    //映射的内存对象
    private MappedByteBuffer mappedByteBuffer;

    //当前写文件的当前写文件的位置位置
    protected final AtomicInteger wrotePosition = new AtomicInteger(0);
    protected final AtomicInteger committedPosition = new AtomicInteger(0);
    private final AtomicInteger flushedPosition = new AtomicInteger(0);

    public MappedFile() {
    }

    public MappedFile(final String fileName, final int fileSize) throws IOException {
        init(fileName, fileSize);
    }

    private void init(final String fileName, final int fileSize) throws IOException {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = new File(fileName);
//        this.fileFromOffset = Long.parseLong(this.file.getName());
        boolean ok = false;

//        ensureDirOK(this.file.getParent());

        try {
            this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(fileSize);
            TOTAL_MAPPED_FILES.incrementAndGet();
            ok = true;
        } catch (FileNotFoundException e) {
            log.error("Failed to create file " + this.fileName, e);
            throw e;
        } catch (IOException e) {
            log.error("Failed to map file " + this.fileName, e);
            throw e;
        } finally {
            if (!ok && this.fileChannel != null) {
                this.fileChannel.close();
            }
        }
    }

    public AppendFeedResult appendMessages(final MessageExt messageExt) {

        int currentPos = this.wrotePosition.get();
        // 表示有空余空间
        if (currentPos < this.fileSize) {
            //共享子序列
            ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
            //起始位置
            byteBuffer.position(currentPos);



            AppendFeedResult result = new AppendFeedResult(AppendMessageStatus.PUT_OK);
            this.wrotePosition.addAndGet(result.getWroteBytes());
            return result;
        }
        log.error("MappedFile.appendMessage return null, wrotePosition: {} fileSize: {}", currentPos, this.fileSize);
        return new AppendFeedResult(AppendMessageStatus.UNKNOWN_ERROR);
    }

}
