package com.example.springcloud_provider.servicempl;
// DatabaseBackupService.java
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class DatabaseBackupService {


    public void backupDatabase() throws IOException, InterruptedException {
        String command = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u root -p1900157363Ckj hotel --result-file=G:\\database_bf\\backup.sql";


        Process process = Runtime.getRuntime().exec(command);

        // 获取标准错误流
        InputStream errorStream = process.getErrorStream();
        InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
        BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader);

        String line;
        while ((line = errorBufferedReader.readLine()) != null) {
            // 处理标准错误输出
            System.err.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Backup completed successfully.");
        } else {
            System.err.println("Backup failed with exit code: " + exitCode);
        }
    }


    public void restoreDatabase() throws IOException, InterruptedException {
        String command = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql --user=root --password=1900157363Ckj hotel --execute=\"source G:\\database_bf\\backup.sql\"";

        Process process = Runtime.getRuntime().exec(command);

        // 获取标准错误流
        InputStream errorStream = process.getErrorStream();
        InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
        BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader);

        String line;
        while ((line = errorBufferedReader.readLine()) != null) {
            // 处理标准错误输出
            System.err.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Restore completed successfully.");
        } else {
            System.err.println("Restore failed with exit code: " + exitCode);
        }
    }

}
