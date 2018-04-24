using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class THESIS
    {
        public THESIS()
        {
            this.DOCUMENTS = new List<DOCUMENT>();
            this.KEYW_THESIS_ASSOCIACIONS = new List<KEYW_THESIS_ASSOCIACIONS>();
        }

        public int ID { get; set; }
        public string ATH_FNAME { get; set; }
        public string ATH_SNAME { get; set; }
        public string ATH_LNAME { get; set; }
        public string ATH_FACNMB { get; set; }
        public int COUNTRY_ID { get; set; }
        public int SPECIALITY_ID { get; set; }
        public int FORM_ID { get; set; }
        public string ATH_EMAIL { get; set; }
        public int SUPERVISOR_ID { get; set; }
        public string TITLE { get; set; }
        public string ANNOTATION { get; set; }
        public System.DateTime DT_ASSIGNED { get; set; }
        public Nullable<System.DateTime> DT_OBTAINED { get; set; }
        public Nullable<double> SCORE { get; set; }
        public virtual COUNTRy COUNTRy { get; set; }
        public virtual ICollection<DOCUMENT> DOCUMENTS { get; set; }
        public virtual EDUCATION_FORMS EDUCATION_FORMS { get; set; }
        public virtual ICollection<KEYW_THESIS_ASSOCIACIONS> KEYW_THESIS_ASSOCIACIONS { get; set; }
        public virtual OFFICIAL OFFICIAL { get; set; }
        public virtual SPECIALITy SPECIALITy { get; set; }
    }
}
