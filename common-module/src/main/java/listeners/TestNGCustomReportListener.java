package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.collections.Lists;
import org.testng.xml.XmlSuite;


import java.io.*;
import java.security.Key;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestNGCustomReportListener implements IReporter {

    private static final Logger LOG = LoggerFactory.getLogger(TestNGCustomReportListener.class);
    // ~ Instance fields ------------------------------------------------------

    private PrintWriter out;
    private int row;
    private Integer testIndex;
    private int methodIndex;
    private Scanner scanner;
    private String lastClassName = "";
    private static HashMap<String, String> testMethodList = new HashMap<>();
    private static ArrayList<String[]> rowCollection = new ArrayList<>();
    private static int numOfPass = 0;
    private static int numOfFail = 0;
    private static int numOfSkip = 0;
    private static String startTime, endTime, totalTime;
    private static StringBuffer summaryHTML = new StringBuffer();
    private static StringBuffer consolidatedSummaryHTML = new StringBuffer(); // in case when you have more than one <tests> in testng file
    private static StringBuffer detailSummaryHTML = new StringBuffer();
    private static boolean isRequiredConsolidatedSummary = false;
    private static int numberOfSuite = 0;
    private static int numOfPassAll = 0;
    private static int numOfFailAll = 0;
    private static int numOfSkipAll = 0;
    private static long startTimeAll = Long.MAX_VALUE;
    private static long endTimeAll, totalTimeAll;
    private static long startTimeDate = 0;
    private static long endTimeDate = 0;
    static String directoryName = System.getProperty("user.dir");

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outdir) {
        System.out.println("Location " + outdir);
        try {
            out = createWriter("target/test-output");
        } catch (IOException e) {
            LOG.error("output file", e);
            return;
        }

        startHtml(out);
        summaryHTML.append("<h2>Summary of each Test run </h2>");
        summaryHTML.append("<table cellspacing=\"0\" cellpadding=\"0\" class = \"testOverview\" style=\"padding-bottom:2em\" width = '1000'>");
        summaryHTML.append("<tr bgcolor = '#ccddff'>");
        summaryHTML.append("<th>Suite</th>");
        summaryHTML.append("<th># Total</th>");
        summaryHTML.append("<th># Passed</th>");
        summaryHTML.append("<th># Skipped</th>");
        summaryHTML.append("<th># Failed</th>");
        summaryHTML.append("<th>Start Time</th>");
        summaryHTML.append("<th>End Time</th>");
        summaryHTML.append("<th>Total Time(hh:mm:ss)</th>");
        summaryHTML.append("</tr>");

        detailSummaryHTML.append("<br><h2>Detail of Test run methods </h2><br>");
        detailSummaryHTML.append("<table cellspacing=\"0\" cellpadding=\"0\" class=\"methodOverview\" id = \"summary\" style=\"padding-bottom:2em\" width = '1000'>");
        detailSummaryHTML.append("<tr bgcolor='#ccddff'><th>Class</th><th>Method</th><th>Status </th><th>Time<br/>(hh:mm:ss)</th></tr>");
        generateMethodSummaryReport(suites);
        summaryHTML.append("</table>");
        detailSummaryHTML.append("</table>");

        ///
        if (numberOfSuite > 1) {

            consolidatedSummaryHTML.append("<table cellspacing=\"0\" cellpadding=\"0\" class = \"testOverview\" style=\"padding-bottom:2em\" width = '1000'>");
            consolidatedSummaryHTML.append("<h2>Consolidated Summary</h2>");
            consolidatedSummaryHTML.append("<tr bgcolor = '#ccddff'>");
            consolidatedSummaryHTML.append("<th># Total</th>");
            consolidatedSummaryHTML.append("<th># Passed</th>");
            consolidatedSummaryHTML.append("<th># Skipped</th>");
            consolidatedSummaryHTML.append("<th># Failed</th>");
            consolidatedSummaryHTML.append("<th>Start Time</th>");
            consolidatedSummaryHTML.append("<th>End Time</th>");
            consolidatedSummaryHTML.append("<th>Total Time(hh:mm:ss)</th>");
            consolidatedSummaryHTML.append("</tr>");

            consolidatedSummaryHTML.append("<tr>");
            consolidatedSummaryHTML.append("<td>");
            consolidatedSummaryHTML.append(numOfPassAll + numOfFailAll + numOfSkipAll);
            consolidatedSummaryHTML.append("</td>");
            consolidatedSummaryHTML.append("<td>");
            consolidatedSummaryHTML.append(numOfPassAll);
            consolidatedSummaryHTML.append("</td>");
            consolidatedSummaryHTML.append("<td>");
            consolidatedSummaryHTML.append(numOfSkipAll);
            consolidatedSummaryHTML.append("</td>");
            consolidatedSummaryHTML.append("<td>");
            consolidatedSummaryHTML.append(numOfFailAll);
            consolidatedSummaryHTML.append("</td>");
            consolidatedSummaryHTML.append("<td>");
            consolidatedSummaryHTML.append(new SimpleDateFormat("kk:mm:ss").format(new Date(startTimeAll)));
            consolidatedSummaryHTML.append("</td>");
            consolidatedSummaryHTML.append("<td>");
            consolidatedSummaryHTML.append(new SimpleDateFormat("kk:mm:ss").format(new Date(endTimeAll)));
            consolidatedSummaryHTML.append("</td>");
            consolidatedSummaryHTML.append("<td>");
            consolidatedSummaryHTML.append(millisToTimeConversion((endTimeAll - startTimeAll) / 1000));
            consolidatedSummaryHTML.append("</td>");
            consolidatedSummaryHTML.append("</tr>");
            consolidatedSummaryHTML.append("</table>");
            out.print(consolidatedSummaryHTML);

        }

        out.print(summaryHTML);
        out.print(detailSummaryHTML);
        endHtml(out);
        out.flush();
        out.close();
    }

    protected PrintWriter createWriter(String outdir) throws IOException {

        new File(outdir).mkdirs();
        LOG.info("Output directory " + outdir);
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "customized-emailable-report.html"))));

    }

    /**
     * Creates a table showing the highlights of each test method with links to
     * the method details
     */
    protected void generateMethodSummaryReport(List<ISuite> suites) {
        methodIndex = 0;

        int testIndex = 1;
        for (ISuite suite : suites) {
            if (suites.size() >= 1) {
                titleRow(suite.getName(), 5);
                isRequiredConsolidatedSummary = true;
            }
            Map<String, ISuiteResult> r = suite.getResults();
            for (ISuiteResult r2 : r.values()) {
                ITestContext testContext = r2.getTestContext();
                String testName = testContext.getName();
                this.testIndex = testIndex;
                resultSummary(suite, testContext);
                testIndex++;
            }
        }
    }

    private void resultSummary(ISuite suite, ITestContext testContext) {
        String[] rowresult = null;
        rowCollection = new ArrayList<>();
        testMethodList = new HashMap<>();
        IResultMap resultMap = null;
        String[] testSuiteSummary = new String[7];
        String testClassName = "";
        int numOfTCinSuite = 0;
        boolean hasAddedSpan = false;
        numOfPass = 0;
        numOfFail = 0;
        numOfSkip = 0;
        startTimeDate = testContext.getStartDate().getTime();
        endTimeDate = testContext.getEndDate().getTime();
        startTime = new SimpleDateFormat("kk:mm:ss").format(testContext.getStartDate());
        endTime = new SimpleDateFormat("kk:mm:ss").format(testContext.getEndDate());
        totalTime = millisToTimeConversion((testContext.getEndDate().getTime() - testContext.getStartDate().getTime()) / 1000);

        resultMap = testContext.getPassedTests();
        for (ITestNGMethod method : getMethodSet(resultMap, suite)) {
            numOfPass++;
            numOfTCinSuite++;
            rowresult = new String[4];
            rowresult[0] = method.getTestClass().getName();
            rowresult[1] = qualifiedName(method);
            rowresult[2] = getStatus(resultMap.getResults(method).toArray(new ITestResult[]{})[0]);
            rowresult[3] = getTotalTimeTakenForTestCase(method, resultMap);
            rowCollection.add(rowresult);
            testClassName = rowresult[0].substring(rowresult[0].lastIndexOf('.') + 1);
            testMethodList.put(rowresult[1], rowresult[2]);
        }
        resultMap = testContext.getFailedTests();
        for (ITestNGMethod method : getMethodSet(resultMap, suite)) {
            numOfFail++;
            numOfTCinSuite++;
            rowresult = new String[4];
            rowresult[0] = method.getTestClass().getName();
            rowresult[1] = qualifiedName(method);
            rowresult[2] = getStatus(resultMap.getResults(method).toArray(new ITestResult[]{})[0]);
            rowresult[3] = getTotalTimeTakenForTestCase(method, resultMap);
            rowCollection.add(rowresult);
            testClassName = rowresult[0].substring(rowresult[0].lastIndexOf('.') + 1);
            testMethodList.put(rowresult[1], rowresult[2]);
        }
        resultMap = testContext.getSkippedTests();
        for (ITestNGMethod method : getMethodSet(resultMap, suite)) {
            if (!testMethodList.containsKey(qualifiedName(method))) {
                numOfSkip++;
                numOfTCinSuite++;
                rowresult = new String[4];
                rowresult[0] = method.getTestClass().getName();
                rowresult[1] = qualifiedName(method);
                rowresult[2] = getStatus(resultMap.getResults(method).toArray(new ITestResult[]{})[0]);
                rowresult[3] = getTotalTimeTakenForTestCase(method, resultMap);
                rowCollection.add(rowresult);
                testClassName = rowresult[0].substring(rowresult[0].lastIndexOf('.') + 1);
                testMethodList.put(rowresult[1], rowresult[2]);
            }
        }
        testSuiteSummary[0] = numOfPass + "";
        testSuiteSummary[1] = numOfSkip + "";
        testSuiteSummary[2] = numOfFail + "";
        testSuiteSummary[3] = startTime;
        testSuiteSummary[4] = endTime;
        testSuiteSummary[5] = totalTime;
        testSuiteSummary[6] = testClassName;
        generateSuiteSummaryReport(testSuiteSummary);
        //startResultSummaryTable("methodOverview");

        for (String[] row : rowCollection) {
            detailSummaryHTML.append("<tr>");
            if (numOfTCinSuite > 1 && !hasAddedSpan) {
                detailSummaryHTML.append("<td rowspan=\"" + numOfTCinSuite + "\">");
                detailSummaryHTML.append(row[0]);
                detailSummaryHTML.append("</td>");
                hasAddedSpan = true;
            } else if (numOfTCinSuite > 1 && hasAddedSpan) {
                // don't any row
            } else {
                detailSummaryHTML.append("<td>");
                detailSummaryHTML.append(row[0]);
                detailSummaryHTML.append("</td>");
            }
            detailSummaryHTML.append("<td>");
            detailSummaryHTML.append(row[1]);
            detailSummaryHTML.append("</td>");
            detailSummaryHTML.append("<td style=\"text-align:right\">");
            detailSummaryHTML.append(row[2]);
            detailSummaryHTML.append("</td>");
            detailSummaryHTML.append("<td class=\"numi\">");
            detailSummaryHTML.append(row[3]);
            detailSummaryHTML.append("</td>");
            detailSummaryHTML.append("</tr>");
        }

        if (numberOfSuite > 0) {
            numOfFailAll = numOfFailAll + numOfFail;
            numOfPassAll = numOfPassAll + numOfPass;
            numOfSkipAll = numOfSkipAll + numOfSkip;
            startTimeAll = Math.min(startTimeDate, startTimeAll);
            endTimeAll = Math.max(endTimeDate, endTimeAll);
        }


    }

    private String getTotalTimeTakenForTestCase(ITestNGMethod method, IResultMap resultMap) {
        long end = Long.MIN_VALUE;
        long start = Long.MAX_VALUE;
        long startMS = 0;
        for (ITestResult testResult : resultMap.getResults(method)) {
            if (testResult.getEndMillis() > end) {
                end = testResult.getEndMillis() / 1000;
            }
            if (testResult.getStartMillis() < start) {
                startMS = testResult.getStartMillis();
                start = startMS / 1000;
            }
        }

        return millisToTimeConversion(end - start);
    }

    private String millisToTimeConversion(long seconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":" + prefixZeroToDigit((int) seconds);
    }

    private String getStatus(ITestResult result) {
        String resultText = null;
        if (result.getStatus() == ITestResult.SUCCESS) {
            resultText = "<font color= 'green' ><b>PASS</b></font>";
        } else if (result.getStatus() == ITestResult.FAILURE) {
            resultText = "<font color= 'red' ><b>FAIL</b></font>";
        } else if (result.getStatus() == ITestResult.SKIP) {
            resultText = "<font color= '#606060' ><b>SKIP</b></font>";
        }


        return resultText;
    }

    private String prefixZeroToDigit(int num) {
        int number = num;
        if (number <= 9) {
            String sNumber = "0" + number;
            return sNumber;
        } else
            return "" + number;

    }

    private String qualifiedName(ITestNGMethod method) {
        StringBuilder addon = new StringBuilder();
        String[] groups = method.getGroups();
        int length = groups.length;
        if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
            addon.append("(");
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    addon.append(", ");
                }
                addon.append(groups[i]);
            }
            addon.append(")");
        }

        return "<b>" + method.getMethodName() + "</b> " + addon;
    }

    private Collection<ITestNGMethod> getMethodSet(IResultMap tests,
                                                   ISuite suite) {
        List<IInvokedMethod> r = Lists.newArrayList();
        List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
        for (IInvokedMethod im : invokedMethods) {
            //System.out.println("suite.getAllInvokedMethods()  .."+im);
            if (tests.getAllMethods().contains(im.getTestMethod())) {
                r.add(im);
            }
        }
        //System.out.println("r ....."+ r.toString());
        //Arrays.sort(r.toArray(new IInvokedMethod[r.size()]), new TestSorter());
        Collections.sort(r, new TestSorter());

        //System.out.println("Sorted Array .."+r.toString());
        List<ITestNGMethod> result = Lists.newArrayList();

        // Add all the invoked methods
        for (IInvokedMethod m : r) {
            for (ITestNGMethod temp : result) {
                if (!temp.equals(m.getTestMethod()))
                    result.add(m.getTestMethod());
            }
        }

        // Add all the methods that weren't invoked (e.g. skipped) that we
        // haven't added yet

        Collection<ITestNGMethod> allMethodsCollection = tests.getAllMethods();
        List<ITestNGMethod> allMethods = new ArrayList<ITestNGMethod>(allMethodsCollection);
        //System.out.println("All methods before sort"+ allMethods.toString());
        Collections.sort(allMethods, new TestMethodSorter());
        //System.out.println("After sorting "+allMethods.toString());

        //for (ITestNGMethod m : tests.getAllMethods()) {
        for (ITestNGMethod m : allMethods) {
            //System.out.println("tests.getAllMethods()  .."+m);
            if (!result.contains(m)) {
                result.add(m);
            }
        }
        //System.out.println("results ....."+ result.toString());
        return result;
    }

    @SuppressWarnings("unused")
    public void generateSuiteSummaryReport(List<ISuite> suites) {
        tableStart("testOverview", null);
        out.print("<h2>Summary of Test run </h2>");
        out.print("<tr bgcolor = '#ccddff'>");
        tableColumnStart("Test");
        tableColumnStart("# Passed");
        //tableColumnStart("Scenarios<br/>Passed");
        tableColumnStart("# Skipped");
        tableColumnStart("# Failed");
        //tableColumnStart("Error messages");
        //tableColumnStart("Parameters");
        tableColumnStart("Start Time");
        tableColumnStart("End Time");
        tableColumnStart("Total Time(hh:mm:ss)");
        // tableColumnStart("Included<br/>Groups");
        //tableColumnStart("Excluded<br/>Groups");

        out.println("</tr>");
        NumberFormat formatter = new DecimalFormat("#,##0.0");
        int qty_tests = 0;
        int qty_pass_m = 0;
        int qty_pass_s = 0;
        int qty_skip = 0;
        int qty_fail = 0;
        long time_start = Long.MAX_VALUE;
        long time_end = Long.MIN_VALUE;
        testIndex = 1;
        for (ISuite suite : suites) {
            if (suites.size() >= 1) {
                titleRow(suite.getName(), 10);
            }
            Map<String, ISuiteResult> tests = suite.getResults();
            for (ISuiteResult r : tests.values()) {
                qty_tests += 1;
                ITestContext overview = r.getTestContext();
                startSummaryRow(overview.getName());
                int q = getMethodSet(overview.getPassedTests(), suite).size();
                qty_pass_m += q;
                summaryCell(q, Integer.MAX_VALUE);
				/*q = overview.getPassedTests().size();
				qty_pass_s += q;
				summaryCell(q, Integer.MAX_VALUE);*/
                q = getMethodSet(overview.getSkippedTests(), suite).size();
                qty_skip += q;
                summaryCell(q, 0);
                q = getMethodSet(overview.getFailedTests(), suite).size();
                qty_fail += q;
                summaryCell(q, 0);

                SimpleDateFormat summaryFormat = new SimpleDateFormat("kk:mm:ss");
                summaryCell(summaryFormat.format(overview.getStartDate()), true);
                out.println("</td>");

                summaryCell(summaryFormat.format(overview.getEndDate()), true);
                out.println("</td>");

                time_start = Math.min(overview.getStartDate().getTime(),
                        time_start);
                time_end = Math.max(overview.getEndDate().getTime(), time_end);
				/*summaryCell(
						formatter.format((overview.getEndDate().getTime() - overview
								.getStartDate().getTime()) / 1000.)
								+ " seconds", true);*/
                summaryCell(millisToTimeConversion((overview.getEndDate().getTime() - overview
                        .getStartDate().getTime()) / 1000), true);

                //summaryCell(overview.getIncludedGroups());
                //summaryCell(overview.getExcludedGroups());
                out.println("</tr>");
                testIndex++;
            }
        }
        if (qty_tests > 1) {
            out.println("<tr class=\"total\"><td>Total</td>");
            summaryCell(qty_pass_m, Integer.MAX_VALUE);
            //summaryCell(qty_pass_s, Integer.MAX_VALUE);
            summaryCell(qty_skip, 0);
            summaryCell(qty_fail, 0);
            //summaryCell(" ", true);
            summaryCell(" ", true);
            summaryCell(" ", true);
            //summaryCell(" ", true);
			/*summaryCell(
					formatter.format(((time_end - time_start) / 1000.) / 60.)
					+ " minutes", true);*/
            summaryCell(millisToTimeConversion(((time_end - time_start) / 1000)), true);
            out.println("<td colspan=\"3\">&nbsp;</td></tr>");
        }
        out.println("</table>");
    }

    private void summaryCell(String v, boolean isgood) {
        out.print("<td class=\"numi" + (isgood ? "" : "_attn") + "\">" + v
                + "</td>");
    }

    private void startSummaryRow(String label) {
        row += 1;
        out.print("<tr>"
                + (row % 2 == 0 ? " class=\"stripe\"" : "")
                + "><td style=\"text-align:left;padding-right:2em\"><b>" + label + "</b>" + "</td>");

    }

    private void summaryCell(int v, int maxexpected) {
        summaryCell(String.valueOf(v), v <= maxexpected);
    }

    private void tableStart(String cssclass, String id) {
        out.println("<table cellspacing=\"0\" cellpadding=\"0\""
                + (cssclass != null ? " class=\"" + cssclass + "\""
                : " style=\"padding-bottom:2em\"")
                + (id != null ? " id=\"" + id + "\"" : "") + " width = '1000'>");
        row = 0;
    }

    private void tableColumnStart(String label) {
        out.print("<th>" + label + "</th>");
    }

    private void titleRow(String label, int cq) {
        // titleRow(label, cq, null);
    }


    /**
     * Starts HTML stream
     */
    protected void startHtml(PrintWriter out) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<title>Automation Report</title>");
        out.println("<style type=\"text/css\">");
        out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
        out.println("td,th {border:1px solid #009;padding:.25em .5em}");
        out.println(".result th {vertical-align:bottom}");
        out.println(".param th {padding-left:1em;padding-right:1em}");
        out.println(".param td {padding-left:.5em;padding-right:2em}");
        out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
        out.println(".numi,.numi_attn {text-align:right}");
        out.println(".total td {font-weight:bold}");
        out.println(".passedodd td {background-color: #0A0}");
        out.println(".passedeven td {background-color: #3F3}");
        out.println(".skippedodd td {background-color: #CCC}");
        out.println(".skippedodd td {background-color: #DDD}");
        out.println(".failedodd td,.numi_attn {background-color: #F33}");
        out.println(".failedeven td,.stripe .numi_attn {background-color: #D00}");
        out.println(".stacktrace {white-space:pre;font-family:monospace}");
        out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
    }

    /**
     * Finishes HTML stream
     */
    protected void endHtml(PrintWriter out) {
        //out.println("<center> Report customized </center>");
        out.println("</body></html>");
    }

    // ~ Inner Classes --------------------------------------------------------

    /**
     * Arranges methods by classname and method name
     */
    private class TestSorter implements Comparator<IInvokedMethod> {
        // ~ Methods
        // -------------------------------------------------------------

        /**
         * Arranges methods by classname and method name
         */
        @Override
        public int compare(IInvokedMethod o1, IInvokedMethod o2) {
            //System.out.println("Comparing " + ((ITestNGMethod) o1).getMethodName() + " " + o1.getDate() + " and " + ((ITestNGMethod) o2).getMethodName() + " " + o2.getDate());
            //return (int) (o1.getDate() - o2.getDate());
            //System.out.println("First method class name "+o1.getTestMethod().getTestClass().getName());
            //System.out.println("second method class name "+o2.getTestMethod().getTestClass().getName());
            int r = o1.getTestMethod().getTestClass().getName().compareTo(o2.getTestMethod().getTestClass().getName());
            //System.out.println("class name compare "+ r);
            if (r == 0) {
                //System.out.println("First method name "+o1.getTestMethod());
                //System.out.println("second method name "+o2.getTestMethod());
                r = o1.getTestMethod().getMethodName().compareTo(o2.getTestMethod().getMethodName());

            }
            return r;
        }

    }

    private class TestMethodSorter implements Comparator<ITestNGMethod> {
        @Override
        public int compare(ITestNGMethod o1, ITestNGMethod o2) {
            //return (int) (o1.getDate() - o2.getDate());
            //System.out.println("First method class name "+o1.getTestClass().getName());
            //System.out.println("second method class name "+o2.getTestClass().getName());
            int r = o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
            //System.out.println("class name compare "+ r);
            if (r == 0) {
                //System.out.println("First method name "+o1.getMethodName());
                //System.out.println("second method name "+o2.getMethodName());
                r = o1.getMethodName().compareTo(o2.getMethodName());
            }
            return r;
        }
    }

    public void generateSuiteSummaryReport(String[] suiteDetail) {
        numberOfSuite++;
        summaryHTML.append("<tr>");
        summaryHTML.append("<td>");
        summaryHTML.append(suiteDetail[6]);
        summaryHTML.append("</td>");
        summaryHTML.append("<td>");
        summaryHTML.append(Integer.parseInt(suiteDetail[0]) + Integer.parseInt(suiteDetail[1]) + Integer.parseInt(suiteDetail[2]));
        summaryHTML.append("</td>");
        summaryHTML.append("<td>");
        summaryHTML.append(suiteDetail[0]);
        summaryHTML.append("</td>");
        summaryHTML.append("<td>");
        summaryHTML.append(suiteDetail[1]);
        summaryHTML.append("</td>");
        summaryHTML.append("<td>");
        summaryHTML.append(suiteDetail[2]);
        summaryHTML.append("</td>");
        summaryHTML.append("<td>");
        summaryHTML.append(suiteDetail[3]);
        summaryHTML.append("</td>");
        summaryHTML.append("<td>");
        summaryHTML.append(suiteDetail[4]);
        summaryHTML.append("</td>");
        summaryHTML.append("<td>");
        summaryHTML.append(suiteDetail[5]);
        summaryHTML.append("</td>");
        summaryHTML.append("</tr>");
    }

}