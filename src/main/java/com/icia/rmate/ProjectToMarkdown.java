package com.icia.rmate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ProjectToMarkdown {

    public static void main(String[] args) {
        String projectPath = "C:\\RMate\\src"; // 병합 대상 디렉토리로 제한
        String outputFilePath = "project_report.txt"; // 출력 파일 경로

        try {
            generateMarkdownFromProject(projectPath, outputFilePath);
            System.out.println("'" + outputFilePath + "' 파일이 생성되었습니다.");
        } catch (IOException e) {
            System.err.println("파일 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public static void generateMarkdownFromProject(String projectPath, String outputFilePath) throws IOException {
        Path rootPath = Paths.get(projectPath);
        if (!Files.exists(rootPath)) {
            System.err.println("Error: Project path not found: " + rootPath.toString());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8))) {
            writer.write("# 프로젝트 개요\n\n");
            writer.write("**프로젝트 경로:** " + projectPath + "\n\n");

            // .gradle 디렉토리 등을 필터링하고, 특정 확장자만 포함
            try (Stream<Path> paths = Files.walk(rootPath)
                    .filter(path -> !path.toString().contains(File.separator + ".gradle" + File.separator))
                    .filter(path -> Files.isDirectory(path) || isTargetFileType(path))) {
                paths.forEach(path -> {
                    String indent = "";
                    try {
                        // rootPath부터의 상대 경로로 디렉토리 깊이 계산
                        String relativePath = rootPath.relativize(path).toString();
                        int level = relativePath.isEmpty() ? 0 : relativePath.split(Pattern.quote(File.separator)).length;
                        indent = (level > 0) ? "  ".repeat(level) : "";

                        if (Files.isDirectory(path)) {
                            if (!path.equals(rootPath)) {
                                writer.write(indent + "- **" + relativePath + "/**\n");
                            }
                        } else {
                            // 파일의 상대 경로를 출력
                            writer.write(indent + "- " + relativePath + "\n");

                            String fileName = path.getFileName().toString();
                            String fileContent = "";

                            // 파일 내용 읽기
                            try {
                                fileContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                            } catch (IOException e) {
                                System.err.println("Error reading file: " + path.toString() + " " + e.getMessage());
                            }

                            // 파일 내용이 있는 경우에만 코드 블록으로 출력
                            if (!fileContent.trim().isEmpty()) {
                                String codeType = getCodeType(fileName);

                                writer.write(indent + "  ```" + codeType + "\n");
                                writer.write(fileContent + "\n");
                                writer.write(indent + "  ```\n\n");
                            }
                        }
                    } catch (IOException e) {
                        try {
                            writer.write(indent + "  (파일 읽기 오류: " + e.getMessage() + ")\n\n");
                        } catch (IOException ex) {
                            System.err.println("Writer error: " + ex.getMessage());
                        }
                    }
                });
            }

            // 생성일 추가
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.write("\n---\n**생성일:** " + now.format(formatter) + "\n");
        }
    }

    private static boolean isTargetFileType(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        return fileName.endsWith(".js") || fileName.endsWith(".java") ||
                fileName.endsWith(".html") || fileName.endsWith(".xml") ||
                fileName.endsWith(".sql");
    }

    private static String getCodeType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".html")) return "html";
        else if (fileName.endsWith(".xml")) return "xml";
        else if (fileName.endsWith(".java")) return "java";
        else if (fileName.endsWith(".js")) return "javascript";
        else if (fileName.endsWith(".sql")) return "sql";
        else return "text";
    }
}
