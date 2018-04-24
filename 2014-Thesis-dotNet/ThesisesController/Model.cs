using System;
using System.Collections.Generic;
using System.Linq;
using ThesesController.Models;

namespace ThesesController
{
    public class Model
    {
        ThesisesContext db;

        public Model(ThesisesContext db)
        {
            this.db = db;
        }

        public List<ThesiesTableContentType> GetThesisesDG(string title, string author,
            string advisor, string faculty, string dep, string spec, string degree, int score)
        {

            var query = (
             from a in db.THESISES
             orderby a.ID
             select new ThesiesTableContentType
             {
                 ID = a.ID,
                 TITLE = a.TITLE,
                 AUTHOR = a.ATH_FNAME.TrimEnd() + " " + a.ATH_LNAME.TrimEnd(),
                 DEGREE = a.SPECIALITy.DEGREE.NAME,
                 ADVISOR = a.OFFICIAL.FIRST_NAME.TrimEnd() + " " + a.OFFICIAL.LAST_NAME.TrimEnd(),
                 FACULTY = a.SPECIALITy.DEPARTAMENT.FACULTy.NAME,
                 DEPARTAMENT = a.SPECIALITy.DEPARTAMENT.NAME,
                 SPECIALITY = a.SPECIALITy.NAME,
                 SCORE = a.SCORE,
                 DT_DEF = a.DT_ASSIGNED
             });


            if (this.StringIsNotEmpty(title))
                query = query.Where(s => s.TITLE.ToUpper().Contains(title.Trim().ToUpper()));

            if (this.StringIsNotEmpty(author))
                query = query.Where(s => s.AUTHOR.ToUpper().Contains(author.Trim().ToUpper()));

            if (this.StringIsNotEmpty(faculty))
                query = query.Where(s => s.FACULTY == faculty);

            if (this.StringIsNotEmpty(spec))
                query = query.Where(s => s.SPECIALITY == spec);

            if (this.StringIsNotEmpty(faculty))
                query = query.Where(s => s.FACULTY == faculty);

            if (this.StringIsNotEmpty(advisor))
                query = query.Where(s => s.ADVISOR == advisor);

            if (this.StringIsNotEmpty(dep))
                query = query.Where(s => s.DEPARTAMENT == dep);

            if (this.StringIsNotEmpty(degree))
                query = query.Where(s => s.DEGREE == degree);

            if (score == 1)
            {
                query = query.Where(s => s.SCORE == null);
            }
            else if (score > 1)
            {
                query = query.Where(s => s.SCORE >= score && s.SCORE < score + 1);
            }

            return query.ToList().Select(a =>
                new ThesiesTableContentType
                {
                    ID = a.ID,
                    TITLE = a.TITLE,
                    AUTHOR = a.AUTHOR.TrimEnd(),
                    DEGREE = a.DEGREE,
                    ADVISOR = a.ADVISOR.TrimEnd(),
                    FACULTY = a.FACULTY,
                    DEPARTAMENT = a.DEPARTAMENT,
                    SPECIALITY = a.SPECIALITY,
                    SCORE = a.SCORE,
                    YEAR = this.DateToYearString(a.DT_DEF)

                }).ToList();

        }

        private String DateToYearString(DateTime? dt)
        {
            if (dt == null)
                return "";

            String res = String.Format("{0:yyyy}", dt);

            return res;
        }


        public List<OfcTableContentType> GetOfficialsDG(
      string name, string faculty, string departament)
        {

            var query = (
             from a in db.OFFICIALS
             orderby a.DEPARTAMENT.FACULTy.ID, a.DEPARTAMENT.ID, a.FIRST_NAME
             select new OfcTableContentType
             {
                 ID = a.ID,
                 NAME = a.FIRST_NAME.TrimEnd() + " " + a.LAST_NAME.TrimEnd(),
                 FACULTY = a.DEPARTAMENT.FACULTy.NAME,
                 DEPARTAMENT = a.DEPARTAMENT.NAME,
                 POSITION = a.POSITION.NAME,
                 FNAME = a.FIRST_NAME.TrimEnd(),
                 LNAME = a.LAST_NAME.TrimEnd(),
                 EMAIL = a.EMAIL
             });


            if (this.StringIsNotEmpty(name))
                query = query.Where(s => s.NAME.ToUpper().Contains(name.Trim().ToUpper()));


            if (this.StringIsNotEmpty(faculty))
                query = query.Where(s => s.FACULTY == faculty);

            if (this.StringIsNotEmpty(departament))
                query = query.Where(s => s.DEPARTAMENT == departament);

            return query.ToList();

        }

        public List<DropDownType> GetFaculties()
        {

            var query = (
     from a in db.FACULTIES
     orderby a.NAME
     select new DropDownType { ID = a.ID, NAME = a.NAME }
             ).ToList();


            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public int GetFacultyId(int specId)
        {

            if (specId == 0)
                return 0;

            var query = (
  from a in db.SPECIALITIES
  where a.ID == specId
  select new { ID = a.DEPARTAMENT.FACULTY_ID }
          ).ToList();


            return query[0].ID;
        }

        public int GetDegreeId(int specId)
        {

            if (specId == 0)
                return 0;

            var query = (
  from a in db.SPECIALITIES
  where a.ID == specId
  select new { ID = a.DEGREE_ID }
          ).ToList();


            return query[0].ID;
        }

        public List<DropDownType> GetDepartaments()
        {

            var query = (
     from a in db.DEPARTAMENTS
     orderby a.NAME
     select new DropDownType { ID = a.ID, NAME = a.NAME }
             ).ToList();


            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetDepartaments(int facId)
        {

            var query = (
     from a in db.DEPARTAMENTS
     where a.FACULTY_ID == facId
     orderby a.NAME
     select new DropDownType { ID = a.ID, NAME = a.NAME }
             ).ToList();


            //query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetDegrees()
        {

            var query = (
     from a in db.DEGREES
     orderby a.ID
     select new DropDownType { ID = a.ID, NAME = a.NAME }
             ).ToList();


            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetSpecialities()
        {

            var query = (
       from a in db.SPECIALITIES
       orderby a.DEGREE_ID, a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.NAME
       select new DropDownType { ID = a.ID, NAME = a.NAME }
       ).ToList();

            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetSpecialities(int degId, int facId)
        {
            List<DropDownType> query;

            if (degId != 0 && facId == 0)
            {
                query = (
          from a in db.SPECIALITIES
          where a.DEGREE_ID == degId
          orderby a.DEGREE_ID, a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.NAME
          select new DropDownType { ID = a.ID, NAME = a.NAME }
          ).ToList();

            }
            else if (degId == 0 && facId != 0)
            {
                query = (
          from a in db.SPECIALITIES
          where a.DEPARTAMENT.FACULTY_ID == facId
          orderby a.DEGREE_ID, a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.NAME
          select new DropDownType { ID = a.ID, NAME = a.NAME }
          ).ToList();

            }
            else if (degId != 0 && facId != 0)
            {
                query = (
          from a in db.SPECIALITIES
          where a.DEPARTAMENT.FACULTY_ID == facId
          where a.DEGREE_ID == degId
          orderby a.DEGREE_ID, a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.NAME
          select new DropDownType { ID = a.ID, NAME = a.NAME }
          ).ToList();

            }
            else
            {
                query = (
           from a in db.SPECIALITIES
           orderby a.DEGREE_ID, a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.NAME
           select new DropDownType { ID = a.ID, NAME = a.NAME }
           ).ToList();
            }

            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetOfficials()
        {

            var query = (
         from a in db.OFFICIALS
         // orderby a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.FIRST_NAME
         orderby a.ID
         select new DropDownType { ID = a.ID, NAME = ((a.FIRST_NAME).Trim() + " " + a.LAST_NAME.Trim()) }
       ).ToList();

            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetOfficials(int depId)
        {

            var query = (
         from a in db.OFFICIALS
         // orderby a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.FIRST_NAME
         where a.DEPARTAMENT_ID == depId
         orderby a.ID
         select new DropDownType { ID = a.ID, NAME = ((a.FIRST_NAME).Trim() + " " + a.LAST_NAME.Trim()) }
       ).ToList();

            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetOfficialsByFac(int facId)
        {

            var query = (
         from a in db.OFFICIALS
         // orderby a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.FIRST_NAME
         where a.DEPARTAMENT.FACULTY_ID == facId
         orderby a.ID
         select new DropDownType { ID = a.ID, NAME = ((a.FIRST_NAME).Trim() + " " + a.LAST_NAME.Trim()) }
       ).ToList();

            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetFileTypes()
        {

            var query = (
         from a in db.DOCUMENT_TYPES
         // orderby a.DEPARTAMENT.FACULTY_ID, a.DEPARTAMENT, a.FIRST_NAME
         orderby a.ID
         select new DropDownType { ID = a.ID, NAME = a.NAME.TrimEnd() }
       ).ToList();

            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetPositions()
        {

            var query = (
     from a in db.POSITIONS
     orderby a.ID
     select new DropDownType { ID = a.ID, NAME = a.NAME }
             ).ToList();


            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetCountries()
        {

            var query = (
     from a in db.COUNTRIES
     orderby a.ID
     select new DropDownType { ID = a.ID, NAME = a.NAME }
             ).ToList();


            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetKeywords()
        {

            var query = (
     from a in db.KEYWORDS
     orderby a.ID
     select new DropDownType { ID = a.ID, NAME = a.WORD }
             ).ToList();


            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DropDownType> GetKeywords(int thesisId)
        {

            var query = (
     from a in db.KEYW_THESIS_ASSOCIACIONS
     where a.THESIS_ID == thesisId
     orderby a.ID
     select new DropDownType { ID = a.ID, NAME = a.KEYWORD.WORD }
             ).ToList();

            return query;
        }

        public void DeleteKeywordAssoc(int id)
        {

            var el = db.KEYW_THESIS_ASSOCIACIONS.Where(s => s.ID == id).First();

            db.KEYW_THESIS_ASSOCIACIONS.Remove(el);

            db.SaveChanges();

        }

        public List<DropDownType> GetEduForms()
        {

            var query = (
     from a in db.EDUCATION_FORMS
     orderby a.ID
     select new DropDownType { ID = a.ID, NAME = a.NAME }
             ).ToList();


            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });

            return query;
        }

        public List<DepTableContentType> GetDepartamentsDG()
        {

            var query = (
     from a in db.DEPARTAMENTS
     orderby a.FACULTy.NAME, a.NAME
     select new DepTableContentType { ID = a.ID, NAME = a.NAME, FACULTY = a.FACULTy.NAME, HEAD_ID = a.HEAD_ID }
             ).ToList();

            return query;
        }

        public List<SpecTableContentType> GetSpecialitiesDG()
        {

            var query = (
     from a in db.SPECIALITIES
     orderby a.NAME
     select new SpecTableContentType { ID = a.ID, NAME = a.NAME, DEGREE = a.DEGREE.NAME, DEPARTAMENT = a.DEPARTAMENT.NAME }
             ).ToList();

            return query;
        }

        public List<FacTableContentType> GetFacultiesDG()
        {

            var query = (
     from a in db.FACULTIES
     orderby a.NAME
     select new FacTableContentType { ID = a.ID, NAME = a.NAME, HEAD_ID = a.HEAD_ID }
             ).ToList();

            return query;
        }


        public List<FilesTableContentType> GetFilesDG(int thesisId)
        {

            var query = (
     from a in db.DOCUMENTS
     where a.THESIS_ID == thesisId
     orderby a.DOCUMENT_TYPES.ID
     select new FilesTableContentType
     {
         ID = a.ID,
         TYPE = a.DOCUMENT_TYPES.NAME,
         OWNER = a.OFFICIAL.FIRST_NAME.TrimEnd() + " " + a.OFFICIAL.LAST_NAME.TrimEnd(),
         DESCRIPTION = a.DESCRIPTION,
         DT_ADDED = a.DT_ADDED,
         CONTENT = a.BINCONTENT,
         ISPUBLIC = a.PUBLIC_ACCESS,
         EXT = a.FILE_EXTENSION.Trim()
     }
             ).ToList();

            query.Insert(0, new FilesTableContentType
     {
     });

            return query;
        }


        public Boolean StringIsNotEmpty(String str)
        {

            if (str == null)
                return false;

            if (str.Trim().Length == 0)
                return false;

            if (str.Trim().Equals("==="))
                return false;

            return true;
        }

        public void InsertOfficial(int depId, int posId,
         string fname, string lname, string email)
        {


            db.OFFICIALS.Add(new OFFICIAL
            {
                DEPARTAMENT_ID = depId,
                POSITION_ID = posId,
                FIRST_NAME = fname,
                LAST_NAME = lname,
                EMAIL = email
            });

            db.SaveChanges();

        }

        public void UpdateOfficial(int id, int depId, int posId,
            string fname, string lname, string email)
        {

            var el = db.OFFICIALS.Where(s => s.ID == id).First();


            el.DEPARTAMENT_ID = depId;
            el.POSITION_ID = posId;
            el.FIRST_NAME = fname;
            el.LAST_NAME = lname;
            el.EMAIL = email;

            db.SaveChanges();

        }


        public void InsertFaculty(string name, int? headId)
        {

            if (headId == 0)
                headId = null;

            db.FACULTIES.Add(new FACULTy
            {
                NAME = name,
                HEAD_ID = headId
            }
            );

            db.SaveChanges();

        }

        public void UpdateFaculty(int id, string name, int? headId)
        {

            var el = db.FACULTIES.Where(s => s.ID == id).First();

            if (headId == 0)
                headId = null;

            el.NAME = name;
            el.HEAD_ID = headId;

            db.SaveChanges();
        }

        public void UpdateDocVisibility(int docId, byte isPublic)
        {

            var el = db.DOCUMENTS.Where(s => s.ID == docId).First();

            el.PUBLIC_ACCESS = isPublic;

            db.SaveChanges();
        }


        public void InsertDepartament(string name, int facId, int? headId)
        {

            if (headId == 0)
                headId = null;

            db.DEPARTAMENTS.Add(new DEPARTAMENT
            {
                NAME = name,
                FACULTY_ID = facId,
                HEAD_ID = headId
            }
            );

            db.SaveChanges();
        }

        public void UpdateDepartament(int id, string name, int facId, int? headId)
        {

            var el = db.DEPARTAMENTS.Where(s => s.ID == id).First();

            if (headId != 0)
                el.HEAD_ID = headId;

            el.NAME = name;
            el.FACULTY_ID = facId;

            db.SaveChanges();
        }

        public void InsertSpeciality(string name, int depId, int degId)
        {

            db.SPECIALITIES.Add(new SPECIALITy
            {
                NAME = name,
                DEPARTAMENT_ID = depId,
                DEGREE_ID = depId
            }
           );

            db.SaveChanges();
        }

        public void UpdateSpeciality(int id, string name, int depId, int degId)
        {
            var el = db.SPECIALITIES.Where(s => s.ID == id).First();

            el.NAME = name;
            el.DEPARTAMENT_ID = depId;
            el.DEGREE_ID = degId;

            db.SaveChanges();
        }

        public int InsertThesis(string fName, string sName, string lName, string facNmb, int cntrId,
            int specId, int formId, string email,
            string title, int supervisorId, string annotation, DateTime dtAsgn,
            DateTime? dtDef, double? score)
        {
            THESIS thesis = new THESIS()
            {
                ATH_FNAME = fName,
                ATH_SNAME = sName,
                ATH_LNAME = lName,
                ATH_FACNMB = facNmb,
                COUNTRY_ID = cntrId,
                FORM_ID = formId,
                SPECIALITY_ID = specId,
                TITLE = title,
                SUPERVISOR_ID = supervisorId,
                ANNOTATION = annotation,
                DT_ASSIGNED = dtAsgn

            };


            if (dtDef != null)
            {
                thesis.DT_OBTAINED = dtDef;
                thesis.SCORE = score;
            }

            if (!String.IsNullOrEmpty(email))
                thesis.ATH_EMAIL = email;


            this.db.THESISES.Add(thesis);
            db.SaveChanges();

            return thesis.ID;
        }


        public void UpdateThesis(int id, string fName, string sName, string lName, string facNmb, int cntrId,
          int specId, int formId, string email,
          string title, int supervisorId, string annotation, DateTime dtAsgn,
          DateTime? dtDef, double? score)
        {


            var el = db.THESISES.Where(s => s.ID == id).First();

            el.ATH_FNAME = fName.TrimEnd();
            el.ATH_SNAME = sName.TrimEnd();
            el.ATH_LNAME = lName.TrimEnd();
            el.ATH_FACNMB = facNmb.TrimEnd();
            el.COUNTRY_ID = cntrId;
            el.FORM_ID = formId;
            el.SPECIALITY_ID = specId;
            el.TITLE = title.TrimEnd();
            el.SUPERVISOR_ID = supervisorId;
            el.ANNOTATION = annotation.TrimEnd();
            el.DT_ASSIGNED = dtAsgn;


            if (dtDef != null)
            {
                el.DT_OBTAINED = dtDef;
                el.SCORE = score;
            }

            if (!String.IsNullOrEmpty(email))
                el.ATH_EMAIL = email.TrimEnd();

            db.SaveChanges();

        }

        public string GetFName(int id)
        {

            if (id == 0)
                return "";

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { NAME = a.ATH_FNAME }
          ).ToList();


            return query[0].NAME;
        }

        public string GetSName(int id)
        {

            if (id == 0)
                return "";

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { NAME = a.ATH_SNAME }
          ).ToList();


            return query[0].NAME;
        }
        public string GetLName(int id)
        {

            if (id == 0)
                return "";

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { NAME = a.ATH_LNAME }
          ).ToList();


            return query[0].NAME;
        }
        public string GetFacNmb(int id)
        {

            if (id == 0)
                return "";

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { NAME = a.ATH_FACNMB }
          ).ToList();


            return query[0].NAME;
        }

        public string GetEmail(int id)
        {

            if (id == 0)
                return "";

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { NAME = a.ATH_EMAIL }
          ).ToList();


            return query[0].NAME;
        }


        public string GetTitle(int id)
        {

            if (id == 0)
                return "";

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { NAME = a.TITLE }
          ).ToList();


            return query[0].NAME;
        }

        public string GetAnnot(int id)
        {

            if (id == 0)
                return "";

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { NAME = a.ANNOTATION }
          ).ToList();


            return query[0].NAME;
        }


        public int GetCountryId(int id)
        {

            if (id == 0)
                return 0;

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { ID = a.COUNTRY_ID }
          ).ToList();


            return query[0].ID;
        }

        public int GetFormId(int id)
        {

            if (id == 0)
                return 0;

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { ID = a.FORM_ID }
          ).ToList();


            return query[0].ID;
        }

        public int GetSpecId(int id)
        {

            if (id == 0)
                return 0;

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { ID = a.SPECIALITY_ID }
          ).ToList();


            return query[0].ID;
        }

        public int GetAdvisorId(int id)
        {

            if (id == 0)
                return 0;

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { ID = a.SUPERVISOR_ID }
          ).ToList();


            return query[0].ID;
        }

        public int GetDepartamentId(int ofcId)
        {

            if (ofcId == 0)
                return 0;

            var query = (
  from a in db.OFFICIALS
  where a.ID == ofcId
  select new { ID = a.DEPARTAMENT_ID }
          ).ToList();


            return query[0].ID;
        }

        public DateTime GetAsignDate(int id)
        {

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { DT = a.DT_ASSIGNED }
          ).ToList();


            return query[0].DT;
        }


        public DateTime? GetDefDate(int id)
        {

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { DT = a.DT_OBTAINED }
          ).ToList();


            if (query.Count == 0)
                return null;

            return query[0].DT;
        }

        public double? GetScore(int id)
        {

            var query = (
  from a in db.THESISES
  where a.ID == id
  select new { SCORE = a.SCORE }
          ).ToList();


            if (query.Count == 0)
                return null;

            return query[0].SCORE;
        }



        public void InsertFile(int thesisId, int typeId, int ownerId, string fileExtension, byte[] file,
            string description, byte isPublic, int size)
        {

            var doc = new DOCUMENT
            {
                THESIS_ID = thesisId,
                TYPE_ID = typeId,
                FILE_EXTENSION = fileExtension,
                BINCONTENT = file,
                SIZE = size,
                PUBLIC_ACCESS = isPublic
            };

            if (ownerId != 0)
                doc.AUTHOR_ID = ownerId;

            if (!String.IsNullOrEmpty(description))
                doc.DESCRIPTION = description;

            db.DOCUMENTS.Add(doc);

            db.SaveChanges();

        }


        public void InsertKeyword(string word, int thesisId)
        {

            KEYWORD kw;
            var res = db.KEYWORDS.Where(a => a.WORD.ToUpper().Trim() == word.ToUpper().Trim());


            if (res == null || res.ToList().Count == 0)
            {
                kw = new KEYWORD()
                {
                    WORD = word.Trim()
                };

                this.db.KEYWORDS.Add(kw);
                this.db.SaveChanges();
            }
            else
            {
                kw = res.First();
            }


            var resB = db.KEYW_THESIS_ASSOCIACIONS.Where(a => a.KEYWORD_ID == kw.ID && a.THESIS_ID == thesisId);

            if (resB.ToList().Count > 0)
                return;


            db.KEYW_THESIS_ASSOCIACIONS.Add(new KEYW_THESIS_ASSOCIACIONS()
            {
                KEYWORD_ID = kw.ID,
                THESIS_ID = thesisId
            });

            db.SaveChanges();

        }


        //create methods above this marker
    }
}

#region Classes to vizualize in the WindowsForms

public class DropDownType
{
    public int ID { get; set; }
    public string NAME { get; set; }
}

public class SpecTableContentType
{
    public int ID { get; set; }
    public string NAME { get; set; }
    public string DEPARTAMENT { get; set; }
    public string DEGREE { get; set; }
}

public class DepTableContentType
{
    public int ID { get; set; }
    public string NAME { get; set; }
    public string FACULTY { get; set; }
    public int? HEAD_ID { get; set; }
}

public class FacTableContentType
{
    public int ID { get; set; }
    public string NAME { get; set; }
    public int? HEAD_ID { get; set; }
}

public class OfcTableContentType
{
    public int ID { get; set; }
    public string NAME { get; set; }
    public string FACULTY { get; set; }
    public string DEPARTAMENT { get; set; }
    public string FNAME { get; set; }
    public string LNAME { get; set; }
    public string POSITION { get; set; }
    public string EMAIL { get; set; }


}
public class ThesiesTableContentType
{
    public int ID { get; set; }
    //public int FACULTY_ID { get; set; }
    //public int ADVISOR_ID { get; set; }
    //public int DEGREE_ID { get; set; }
    //public int SPECIALITY_ID { get; set; }
    public string TITLE { get; set; }
    public string AUTHOR { get; set; }
    public string ADVISOR { get; set; }
    public string DEGREE { get; set; }
    public string FACULTY { get; set; }
    public string DEPARTAMENT { get; set; }
    public string SPECIALITY { get; set; }
    public double? SCORE { get; set; }
    public string YEAR { get; set; }
    public DateTime? DT_DEF { get; set; }
}

public class FilesTableContentType
{
    public int ID { get; set; }
    public string TYPE { get; set; }
    public string OWNER { get; set; }
    public string DESCRIPTION { get; set; }
    public string EXT { get; set; }
    public DateTime? DT_ADDED { get; set; }
    public byte[] CONTENT { get; set; }
    public byte ISPUBLIC { get; set; }

}

#endregion


