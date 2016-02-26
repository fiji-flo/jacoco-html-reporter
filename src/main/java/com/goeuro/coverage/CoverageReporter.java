package com.goeuro.coverage;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.InputStreamSourceFileLocator;
import org.jacoco.report.html.HTMLFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CoverageReporter {
    public static class DirectoriesSourceFileLocator extends InputStreamSourceFileLocator {

        private final String[] directories;

        public DirectoriesSourceFileLocator(final String[] directories,
        final String encoding, final int tabWidth) {
            super(encoding, tabWidth);
            this.directories = directories;
        }

        @Override
        protected InputStream getSourceStream(final String path) throws IOException {
            for (String directory: directories) {
                final File file = new File(directory, path);
                if (file.exists()) {
                    return new FileInputStream(file);
                }
            }
            return null;
        }
    }

    private static void create(String outDir, String[] execFiles, String[] classDirs, String[] srcDirs, HTMLFormatter formatter)  throws IOException {
        IReportVisitor visitor = formatter
        .createVisitor(new FileMultiReportOutput(new File(outDir, "report")));
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        ExecFileLoader execFileLoader = new ExecFileLoader();
        for (String execFile: execFiles) {
            execFileLoader.load(new File(execFile));
        }
        Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);

        for (String classDir: classDirs) {
            analyzer.analyzeAll(new File(classDir));
        }

        visitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),
        execFileLoader.getExecutionDataStore().getContents());


        IBundleCoverage bundle = coverageBuilder.getBundle("report");
        visitor.visitBundle(bundle, new DirectoriesSourceFileLocator(srcDirs, "utf-8", 4));

        visitor.visitEnd();
    }

    public static void main(String... args) {
        if (args.length != 4) {
            System.out.println("usage OUTDIR EXECS CLASSES SOURCES");
            return;
        }
        try {
            create(
            args[0],
            args[1].split(";"),
            args[2].split(";"),
            args[3].split(";"), new HTMLFormatter()
            );
        } catch (IOException e) {
            System.out.print("Unable to generate report: ");
            System.out.print(e);
        }
    }
}
