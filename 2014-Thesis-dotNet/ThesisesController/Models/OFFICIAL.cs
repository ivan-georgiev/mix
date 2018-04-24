using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class OFFICIAL
    {
        public OFFICIAL()
        {
            this.DEPARTAMENTS = new List<DEPARTAMENT>();
            this.DOCUMENTS = new List<DOCUMENT>();
            this.FACULTIES = new List<FACULTy>();
            this.PASSWORDS = new List<PASSWORD>();
            this.THESISES = new List<THESIS>();
        }

        public int ID { get; set; }
        public string FIRST_NAME { get; set; }
        public string LAST_NAME { get; set; }
        public int POSITION_ID { get; set; }
        public int DEPARTAMENT_ID { get; set; }
        public string EMAIL { get; set; }
        public virtual ICollection<DEPARTAMENT> DEPARTAMENTS { get; set; }
        public virtual DEPARTAMENT DEPARTAMENT { get; set; }
        public virtual ICollection<DOCUMENT> DOCUMENTS { get; set; }
        public virtual ICollection<FACULTy> FACULTIES { get; set; }
        public virtual POSITION POSITION { get; set; }
        public virtual ICollection<PASSWORD> PASSWORDS { get; set; }
        public virtual ICollection<THESIS> THESISES { get; set; }
    }
}
