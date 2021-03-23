using EHBsBDDFramework.Constants;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Framework.Utility
{
    public class DataManager
    {
        /*
     public static GranteeEntity FindGrantee()
     {
         using (DBConnection DB = new DBConnection())
         {
             string query = QueryScripts.PAR_Find_A_Grantee;
             List<GranteeEntity> data = DB.GetTable(query).ToList<GranteeEntity>();
             return data.FirstOrDefault();
         }
     }

     public static ReviewerEntity FindReviewer(RoleType reviewerRole, ReviewerType reviewerType, string TrackingNumber)
     {
         using (DBConnection DB = new DBConnection())
         {
             string query = string.Empty;
             switch (reviewerType)
             {
                 case ReviewerType.Individual:
                 case ReviewerType.TeamAssigned:
                     query = QueryScripts.PAR_Find_A_Reviewer_Individual + " WHERE task.TaskStatusCode!=3 AND par.TrackingNumber in ('" + TrackingNumber + "') AND usrRole.RoleId = '" + Convert.ToInt32(reviewerRole) + "'";
                     break;
                 case ReviewerType.TeamUnassigned:
                     query = QueryScripts.PAR_Find_A_Reviewer_Team + " WHERE task.TaskStatusCode!=3 AND par.TrackingNumber in ('" + TrackingNumber + "') AND usrRole.RoleId = '" + Convert.ToInt32(reviewerRole) + "'"
                             + " ORDER BY ReviewerUserName desc";
                     break;
             }

             List<ReviewerEntity> data = DB.GetTable(query).ToList<ReviewerEntity>();
             return data.FirstOrDefault();
         }
     }

     public static ReviewerEntity FindCurrentReviewer(string TrackingNumber)
     {
         using (DBConnection DB = new DBConnection())
         {
             string query = QueryScripts.PAR_Find_Current_Reviewer + " WHERE t.TaskStatusCode!=3 AND TrackingNumber in ('" + TrackingNumber + "')";
             List<ReviewerEntity> data = DB.GetTable(query).ToList<ReviewerEntity>();

             var CurrentNonGMSRole = data.Where(r=>!r.Role.Equals("GMS")).ToList().FirstOrDefault();
             if (CurrentNonGMSRole!= null)
             {
                 return CurrentNonGMSRole;
             }
             else
             {
                 var CurrentGMSRole = data.Where(r=>r.Role.Equals("GMS")).ToList().FirstOrDefault();
                 return CurrentGMSRole != null ? CurrentGMSRole : null;
             }
         }
     }

     public static RoleType FindCurrentReviewerRole(string TrackingNumber)
     {
         RoleType currentReviewerRole = 0;
         if (!string.IsNullOrEmpty(TrackingNumber))
         {
             using (DBConnection DB = new DBConnection())
             {
                 var currentReviewer = FindCurrentReviewer(TrackingNumber);
                 if (currentReviewer != null)
                     currentReviewerRole = GetRoleType(currentReviewer.Role);
             }
         }
         return currentReviewerRole;
     }


     public static void SaveRequestTest(PriorApprovalTestGroup TestGroup)
     {
         string wbk = ConfigManager.DataPath();
         string wksRequest = "PA_Requests_TestData";
         string wksReview = "PA_Reviews_TestData";
         ExcelHelper.WriteRequestTestResult(wbk, wksRequest, wksReview, TestGroup);
     }

     public static string GetTrackingNumber(int rowNo)
     {
         string wbk = ConfigManager.DataPath();
         string wks = "PA_Requests_TestData";
         return ExcelHelper.GetCellData(wbk, wks, rowNo, 4);
     }

     public static string GetTrackingNumber()
     {
         string trackingNumber = string.Empty;
         using (ExcelConnection DB = new ExcelConnection())
         {
             string query = "SELECT * FROM [PA_Reviews_TestData$] WHERE [SN] IS NOT NULL AND [ReviewTestType] IS NULL ORDER BY SN";

             DataTable dt = DB.GetTable(query);

             if (dt.Rows.Count>0)
                 trackingNumber = (dt.Rows.Count > 0) ? (dt.Rows[0]["TrackingNumber"]).ToString() : string.Empty;
         }
         return trackingNumber;
     }

     public static void SaveReviewTest(PriorApprovalTestGroup TestGroup)
     {
         int rowNo = GetRowNo(TestGroup.Request.TrackingNumber);
         if (rowNo > 0)
         {
             string wbk = ConfigManager.DataPath();
             string wks = "PA_Reviews_TestData";
             ExcelHelper.WriteReviewTestResult(wbk, wks, TestGroup, rowNo);
         }
         else
         {
             throw new Exception("Unable to get Excel row number of the tracking number");
         }
     }

     public static void LogTestResultToFile(PriorApprovalTestGroup testGroup)
     {
         if (testGroup.Results.Count > 0)
         {
             string filePath = ConfigManager.ReportsPath();
             string fileName = testGroup.Context.ReportFilePrefix + DateTime.Now.ToShortDateString().Replace('/', '-');

             using (StreamWriter file = File.AppendText(filePath + fileName + ".txt"))
             {
                 int colWidth = 25;
                 file.WriteLine();
                 file.WriteLine("-------------------------------------------------// Start New Test //-------------------------------------------------");
                 file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Time".PadRight(colWidth, '.'), DateTime.Now.ToString()));
                 file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Test Group".PadRight(colWidth, '.'), testGroup.Context.TestGroupName));
                 file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Environment".PadRight(colWidth, '.'), testGroup.Context.TestEnvironment));
                 file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Machine".PadRight(colWidth, '.'), Environment.MachineName));
                 file.WriteLine();

                 switch ((TestType)testGroup.Context.TestType)
                 {
                     case TestType.PriorApprovalRequest:
                         if (testGroup.Request != null)
                         {
                             file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Grant Number".PadRight(colWidth, '.'), (testGroup.Request.Grantee != null && !string.IsNullOrEmpty(testGroup.Request.Grantee.GrantNumber)) ? testGroup.Request.Grantee.GrantNumber : string.Empty));
                             file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Grantee UserName".PadRight(colWidth, '.'), (testGroup.Request.Grantee != null && !string.IsNullOrEmpty(testGroup.Request.Grantee.GranteeUserName)) ? testGroup.Request.Grantee.GranteeUserName : string.Empty));
                             file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Request Type".PadRight(colWidth, '.'), GetRequestTypeName((PriorApprovalRequestType)testGroup.Request.RequestType)));
                             file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Tracking Number".PadRight(colWidth, '.'), (!string.IsNullOrEmpty(testGroup.Request.TrackingNumber)) ? testGroup.Request.TrackingNumber : string.Empty));
                             file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Test Result".PadRight(colWidth, '.'), testGroup.Passed ? "Passed" : "Failed"));
                         }
                         break;
                     case TestType.PriorApprovalReview:
                         file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Tracking Number".PadRight(colWidth, '.'), (!string.IsNullOrEmpty(testGroup.Request.TrackingNumber)) ? testGroup.Request.TrackingNumber : string.Empty));
                         file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Review Test Type".PadRight(colWidth, '.'), GetReviewTestTypeName((ReviewTestType)testGroup.ReviewTestType)));
                         file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Reviewer Option".PadRight(colWidth, '.'), !string.IsNullOrEmpty(testGroup.ReviewerAssignmentOption) ? testGroup.ReviewerAssignmentOption : string.Empty));
                         file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Review Workflow".PadRight(colWidth, '.'), !string.IsNullOrEmpty(testGroup.ReviewWorkflow) ? testGroup.ReviewWorkflow : string.Empty));
                         file.WriteLine(String.Format("{0,-" + colWidth + "} {1}", "Test Result".PadRight(colWidth, '.'), ((testGroup.Results.Count > 0) && !(testGroup.Results.Any(r => r.Result == false))) ? "Passed" : "Failed"));
                         file.WriteLine();
                         if (testGroup.Reviews != null && testGroup.Reviews.Count > 0)
                         {
                             file.WriteLine("Reviewers:");
                             file.WriteLine(String.Format("{0,-12} {1,-30} {2,-10} {3,-25} {4,-50}",
                                 "Reviewer#".PadRight(12, '.'),
                                 "UserName".PadRight(30, '.'),
                                 "Role".PadRight(10, '.'),
                                 "Recommendation".PadRight(25, '.'),
                                 "TeamName" 
                                 ));
                             foreach (var review in testGroup.Reviews)
                             {
                                 if (review.Reviewer != null && !string.IsNullOrEmpty(review.Reviewer.ReviewerUserName))
                                 {
                                     file.WriteLine(String.Format("{0,-12} {1,-30} {2,-10} {3,-25} {4,-50}",
                                         review.ReviewSequence.ToString().PadRight(12, '.'),
                                         review.Reviewer.ReviewerUserName.PadRight(30, '.'),
                                         review.Reviewer.Role.PadRight(10, '.'),
                                         GetReviewRecommendationName((ReviewRecommendations)review.ReviewRecommendation).PadRight(25, '.'),
                                         review.Reviewer.TeamName
                                         ));
                                 }
                             }
                         }
                         break;
                 }

                 file.WriteLine();
                 file.WriteLine();

                 //log test results
                 List<TestResult> Results = testGroup.Results.Where(r => r.ErrorStatus == false).ToList();
                 if (Results.Count != 0)
                 {
                     file.WriteLine("Test Results: ");
                     foreach (var test in Results)
                     {
                         file.WriteLine(String.Format("{0,-68} {1,-25} {2,-15}", test.Name.PadRight(68, '.'), test.DateTimeStamp.PadRight(25, '.'), (test.Result ? "Passed" : "Failed")));
                     }
                 }

                 //log exceptions
                 List<TestResult> Exceptions = testGroup.Results.Where(r => r.ErrorStatus == true).ToList();
                 if (Exceptions.Count != 0)
                 {
                     file.WriteLine();
                     file.WriteLine("Exceptions:");
                     foreach (var ex in Exceptions)
                     {
                         file.WriteLine("Test Name: " + ex.Name);
                         file.WriteLine(ex.ErrMessage);

                     }
                 }
                 file.WriteLine("----------------------------------------------------// End Test //---------------------------------------------------");
                 file.WriteLine();
             }
         }
     }

        public static int GetRowNo(string trackingNumber)
        {
            int rowNo = 0;
            using (ExcelConnection DB = new ExcelConnection())
            {
                string query = "SELECT * FROM [PA_Reviews_TestData$] WHERE [TrackingNumber] = '" + trackingNumber + "'";
                DataTable dt = DB.GetTable(query);
                if (dt.Rows.Count > 0)
                    rowNo = (dt.Rows.Count > 0) ? Convert.ToInt32((dt.Rows[0]["SN"])): 0;
                DB.Close();
                DB.Dispose();
            }
            return (rowNo>0)? (++rowNo):(rowNo);
        }*/

        public static string GetReviewerTypeName(ReviewerType reviewerType)
        {
            string reviewerTypeName = string.Empty;
            switch (reviewerType)
            {
                case ReviewerType.Individual:
                    reviewerTypeName = PriorApprovalConstatnts.ReviewerType_Individual;
                    break;
                case ReviewerType.TeamAssigned:
                    reviewerTypeName = PriorApprovalConstatnts.ReviewerType_TeamAssigned;
                    break;
                case ReviewerType.TeamUnassigned:
                    reviewerTypeName = PriorApprovalConstatnts.ReviewerType_TeamUnassigned;
                    break;
            }
            return reviewerTypeName;
        }

        public static string GetReviewerAssignmentOption(ReviewerType reviewerType)
        {
            string reviewerTypeName = string.Empty;

            switch (reviewerType)
            {
                case ReviewerType.Individual:
                    reviewerTypeName = "Individual";
                    break;
                case ReviewerType.TeamAssigned:
                    reviewerTypeName = "TeamAssigned";
                    break;
                case ReviewerType.TeamUnassigned:
                    reviewerTypeName = "TeamUnassigned";
                    break;
            }

            return reviewerTypeName;
        }

        public static string GetReviewTypeName(TestType testType)
        {
            string testTypeName = string.Empty;
            switch (testType)
            {
                case TestType.PriorApprovalRequest:
                    testTypeName = PriorApprovalConstatnts.TestType_Request;
                    break;
                case TestType.PriorApprovalReview:
                    testTypeName = PriorApprovalConstatnts.TestType_Review;
                    break;
            }
            return testTypeName;
        }

        public static string GetReviewRecommendationName(ReviewRecommendations reviewRecommendation)
        {
            string reviewRecommendationName = string.Empty;
            switch (reviewRecommendation)
            {
                case ReviewRecommendations.Approve:
                    reviewRecommendationName = PriorApprovalConstatnts.ReviewerRecommendation_Approve;
                    break;
                case ReviewRecommendations.Disapprove:
                    reviewRecommendationName = PriorApprovalConstatnts.ReviewerRecommendation_Disapprove;
                    break;
                case ReviewRecommendations.NoDecision:
                    reviewRecommendationName = PriorApprovalConstatnts.ReviewerRecommendation_NoDecision;
                    break;
                case ReviewRecommendations.RecommendApproval:
                    reviewRecommendationName = PriorApprovalConstatnts.ReviewerRecommendation_RecommendApproval;
                    break;
                case ReviewRecommendations.RequestChange:
                    reviewRecommendationName = PriorApprovalConstatnts.ReviewerRecommendation_RequestChange;
                    break;
            }
            return reviewRecommendationName;
        }

        public static int GetColumnNo()
        {
            int colNo = 1;
            //get col no
            return colNo;
        }

        public static RoleType GetRoleType(string role)
        {
            RoleType roleType = 0;
            switch (role)
            {
                case "PO":
                    roleType = RoleType.PO;
                    break;
                case "PQC":
                    roleType = RoleType.PQC;
                    break;
                case "PAO":
                    roleType = RoleType.PAO;
                    break;
                case "GMS":
                    roleType = RoleType.GMS;
                    break;
            }
            return roleType;
        }

        public static string GetRoleName(RoleType role)
        {
            string roleName = string.Empty;
            switch (role)
            {
                case RoleType.PO:
                    roleName = "PO";
                    break;
                case RoleType.PQC:
                    roleName = "PQC";
                    break;
                case RoleType.PAO:
                    roleName = "PAO";
                    break;
                case RoleType.GMS:
                    roleName = "GMS";
                    break;
            }
            return roleName;
        }
        public static string GMSReviewDecision(String GMSReviewDecision)
        {
            string Decision = string.Empty;
            switch (GMSReviewDecision)
            {
                case "Accepted":
                    Decision = "1";
                    break;
                case "Pending Decision":
                    Decision = "3";
                    break;
                case "Awaiting Final Financial Report":
                    Decision = "4";
                    break;
                case "Change Requested":
                    Decision = "2";
                    break;
            }
            return Decision;
        }

        public static string GetRequestTypeName(PriorApprovalRequestType requestType)
        {
            string requestTypeName = string.Empty;

            switch (requestType)
            {
                case PriorApprovalRequestType.PD:
                    requestTypeName = "PD";
                    break;
                case PriorApprovalRequestType.Other:
                    requestTypeName = "Other";
                    break;
            }

            return requestTypeName;
        }

        public static string GetReviewTestTypeName(ReviewTestType reviewTestType)
        {
            string reviewTestTypeName = string.Empty;

            switch (reviewTestType)
            {
                case ReviewTestType.Approval:
                    reviewTestTypeName = "Approval";
                    break;
                case ReviewTestType.ChangeRequestFromPreviousReviewer:
                    reviewTestTypeName = "ChangeRequestFromPreviousReviewer";
                    break;
            }

            return reviewTestTypeName;
        }
        public static string GetStatusCode(SubmissionCode submissioncode)
        {
            string requestsubmissioncode = string.Empty;

            switch (submissioncode)
            {
                case SubmissionCode.NotStarted:
                    requestsubmissioncode = "1";
                    break;
                case SubmissionCode.InProgress:
                    requestsubmissioncode = "2";
                    break;
                case SubmissionCode.DataEntryInProgress:
                    requestsubmissioncode = "3";
                    break;
                case SubmissionCode.ReviewInProgress:
                    requestsubmissioncode = "4";
                    break;
                case SubmissionCode.Processed:
                    requestsubmissioncode = "5";
                    break;
                case SubmissionCode.ChangeRequested:
                    requestsubmissioncode = "6";
                    break;
                case SubmissionCode.AdministrativelyClosed:
                    requestsubmissioncode = "10";
                    break;
            }
            return requestsubmissioncode;
        }
        /*    public static InternalReviewerRoleEntity FindInternalReviewer(RoleType roleType , TaskTypeCode taskTypeCode)
            {
                using (DBConnection DB = new DBConnection())
                {

                    string query = string.Format(QueryScripts.Internal_Reviewer, (int)roleType, (int)taskTypeCode);
                    List<InternalReviewerRoleEntity> data = DB.GetTable(query).ToList<InternalReviewerRoleEntity>();
                    return data.FirstOrDefault();
                }
            }*/

    }
    public enum RoleType : int
    {
        GMS = 6,
        PO = 9,
        PAO = 41,
        PQC = 44,
        DIR_REVIEWER = 29,
        QC_REVIEWER = 10,
        GMO = 5,
        GA = 23////
    }

    public enum PriorApprovalRequestType : int
    {
        PD = 3,
        Other = 7
    }

    public enum ReviewerType : int
    {
        TeamUnassigned = 1,
        TeamAssigned = 2,
        Individual = 3
    }

    public enum PriorApprovalReviewType : int
    {
        Internal =1,
        ProgramOffice = 2,
        GrantOffice = 3
    }

    public enum WorkflowOPtions : int
    {
        SendForFurtherReviewToPrograms = 1,
        SendForFurtherReviewToGrants = 2,
        MarkAsComplete=3,
        RequestChangeFromPreviousReviewer=4
    }

    public enum ReviewRecommendations : int
    {
        RecommendApproval = 1,
        Approve = 2,
        Disapprove=3,
        RequestChange=4,
        NoDecision=5
    }

    public enum GrantRecommendations : int
    {
        NonMonetaryAdministrativeChanges = 1
    }

    public enum ReviewTestType : int
    {
        Approval = 1,
        ChangeRequestFromPreviousReviewer = 2
    }

    public enum TestType : int
    {
        PriorApprovalRequest = 1,
        PriorApprovalReview = 2
    }
    public enum TaskTypeCode : int
    {
        PrepareNoA = 1,
        QCReview = 4,
        SignAwards = 5,
    }

    public enum SubmissionCode : int
    {
        NotStarted = 1,
        InProgress = 2,
        DataEntryInProgress=3,
        ReviewInProgress = 4,
        Processed = 5,
        ChangeRequested = 6,
        AdministrativelyClosed=10,
    }

    public class TestEnvironment
    {
        public const string ExternalUTL2 = "externalutl2";
        public const string ExternalUTL9 = "externalutl9";
        public const string ExternalUTL16 = "externalutl16";
        public const string ExternalREIINT = "externalreiint";
        public const string InternalUTL2 = "internalutl2";
        public const string InternalUTL9 = "internalutl9";
        public const string InternalUTL16 = "internalutl16";
        public const string InternalSBX8 = "internalsbx8";
        public const string InternalREIINT = "internalreiint";
        public const string InternalHRSAINT = "internalhrsaint";
        public const string InternalHRSAQA = "internalhrsaqa";
        public const string InternalHRSAOS = "internalhrsaos";
        public const string GrantdotGov = "grantdotgov";
        public const string PMS = "pms";
    }
}