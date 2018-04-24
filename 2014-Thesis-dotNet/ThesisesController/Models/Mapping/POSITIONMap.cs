using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class POSITIONMap : EntityTypeConfiguration<POSITION>
    {
        public POSITIONMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(30);

            // Table & Column Mappings
            this.ToTable("POSITIONS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.NAME).HasColumnName("NAME");
        }
    }
}
