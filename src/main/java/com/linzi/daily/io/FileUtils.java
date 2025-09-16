package com.linzi.daily.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<File> visitAllFiles(String path,String fileType){
        List<File> fileList = new ArrayList<>();
        Path rootDir = Paths.get(path);
        try {
            Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // 当访问到一个文件时调用此方法
                    if (file.toString().toLowerCase().endsWith(fileType)) {
                        System.out.println("找到文件："+ file.toAbsolutePath());
                        fileList.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // 当访问到一个目录时调用此方法（在遍历其内容之前）
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // 如果无法访问某个文件或目录，则会调用此方法
                    System.err.println(exc);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileList;
    }
}
