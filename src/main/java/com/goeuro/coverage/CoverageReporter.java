package com.goeuro.coverage;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;

import java.io.File;
import java.io.IOException;

public class CoverageReporter {
    private static void create(String outDir, String[] execFiles, String[] classDirs, String[] srcDirs)  throws IOException {
        HTMLFormatter htmlFormatter = new HTMLFormatter();
        IReportVisitor visitor = htmlFormatter
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
        for (String srcDir: srcDirs) {
            visitor.visitBundle(bundle, new DirectorySourceFileLocator(
                    new File(srcDir), "utf-8", 4));
        }

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
                    args[3].split(";")
            );
        } catch (IOException e) {
            System.out.print("Unable to generate report: ");
            System.out.print(e);
        }
    }
}
