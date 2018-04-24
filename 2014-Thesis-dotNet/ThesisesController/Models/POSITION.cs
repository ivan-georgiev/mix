using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class POSITION
    {
        public POSITION()
        {
            this.OFFICIALS = new List<OFFICIAL>();
        }

        public int ID { get; set; }
        public string NAME { get; set; }
        public virtual ICollection<OFFICIAL> OFFICIALS { get; set; }
    }
}
