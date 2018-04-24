using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class THESISMap : EntityTypeConfiguration<THESIS>
    {
        public THESISMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.ATH_FNAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(30);

            this.Property(t => t.ATH_SNAME)
                .IsFixedLength()
                .HasMaxLength(30);

            this.Property(t => t.ATH_LNAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(30);

            this.Property(t => t.ATH_FACNMB)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(17);

            this.Property(t => t.ATH_EMAIL)
                .IsFixedLength()
                .HasMaxLength(30);

            this.Property(t => t.TITLE)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(200);

            this.Property(t => t.ANNOTATION)
                .IsFixedLength()
                .HasMaxLength(500);

            // Table & Column Mappings
            this.ToTable("THESISES");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.ATH_FNAME).HasColumnName("ATH_FNAME");
            this.Property(t => t.ATH_SNAME).HasColumnName("ATH_SNAME");
            this.Property(t => t.ATH_LNAME).HasColumnName("ATH_LNAME");
            this.Property(t => t.ATH_FACNMB).HasColumnName("ATH_FACNMB");
            this.Property(t => t.COUNTRY_ID).HasColumnName("COUNTRY_ID");
            this.Property(t => t.SPECIALITY_ID).HasColumnName("SPECIALITY_ID");
            this.Property(t => t.FORM_ID).HasColumnName("FORM_ID");
            this.Property(t => t.ATH_EMAIL).HasColumnName("ATH_EMAIL");
            this.Property(t => t.SUPERVISOR_ID).HasColumnName("SUPERVISOR_ID");
            this.Property(t => t.TITLE).HasColumnName("TITLE");
            this.Property(t => t.ANNOTATION).HasColumnName("ANNOTATION");
            this.Property(t => t.DT_ASSIGNED).HasColumnName("DT_ASSIGNED");
            this.Property(t => t.DT_OBTAINED).HasColumnName("DT_OBTAINED");
            this.Property(t => t.SCORE).HasColumnName("SCORE");

            // Relationships
            this.HasRequired(t => t.COUNTRy)
                .WithMany(t => t.THESISES)
                .HasForeignKey(d => d.COUNTRY_ID);
            this.HasRequired(t => t.EDUCATION_FORMS)
                .WithMany(t => t.THESISES)
                .HasForeignKey(d => d.FORM_ID);
            this.HasRequired(t => t.OFFICIAL)
                .WithMany(t => t.THESISES)
                .HasForeignKey(d => d.SUPERVISOR_ID);
            this.HasRequired(t => t.SPECIALITy)
                .WithMany(t => t.THESISES)
                .HasForeignKey(d => d.SPECIALITY_ID);

        }
    }
}
