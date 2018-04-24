using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class DEPARTAMENT
    {
        public DEPARTAMENT()
        {
            this.OFFICIALS = new List<OFFICIAL>();
            this.SPECIALITIES = new List<SPECIALITy>();
        }

        public int ID { get; set; }
        public int FACULTY_ID { get; set; }
        public string NAME { get; set; }
        public Nullable<int> HEAD_ID { get; set; }
        public string DESRIPTION { get; set; }
        public virtual FACULTy FACULTy { get; set; }
        public virtual OFFICIAL OFFICIAL { get; set; }
        public virtual ICollection<OFFICIAL> OFFICIALS { get; set; }
        public virtual ICollection<SPECIALITy> SPECIALITIES { get; set; }
    }
}
