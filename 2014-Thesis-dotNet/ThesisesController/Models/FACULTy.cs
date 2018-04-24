using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class FACULTy
    {
        public FACULTy()
        {
            this.DEPARTAMENTS = new List<DEPARTAMENT>();
        }

        public int ID { get; set; }
        public Nullable<int> HEAD_ID { get; set; }
        public string NAME { get; set; }
        public string DESCRIPTION { get; set; }
        public virtual ICollection<DEPARTAMENT> DEPARTAMENTS { get; set; }
        public virtual OFFICIAL OFFICIAL { get; set; }
    }
}
