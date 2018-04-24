using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class DEGREEMap : EntityTypeConfiguration<DEGREE>
    {
        public DEGREEMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(20);

            // Table & Column Mappings
            this.ToTable("DEGREES");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.NAME).HasColumnName("NAME");
        }
    }
}
