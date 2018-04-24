using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class SPECIALITy
    {
        public SPECIALITy()
        {
            this.THESISES = new List<THESIS>();
        }

        public int ID { get; set; }
        public string NAME { get; set; }
        public int DEPARTAMENT_ID { get; set; }
        public int DEGREE_ID { get; set; }
        public virtual DEGREE DEGREE { get; set; }
        public virtual DEPARTAMENT DEPARTAMENT { get; set; }
        public virtual ICollection<THESIS> THESISES { get; set; }
    }
}
