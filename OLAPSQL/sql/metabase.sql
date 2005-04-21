drop table  meta_element;
drop table meta_attribute;
drop table meta_star;
drop table meta_hierarchy;
drop table meta_measure;
drop table meta_level;
drop sequence seq_elmt;
drop sequence seq_att;

---------------------------
-- CREATION METABASE
---------------------------

create   table meta_element (
      id number(5),
      name varchar2(30),
      typ varchar(1)
);

ALTER Table meta_element
      ADD Constraint pk_m_elmt PRIMARY KEY (id)
;
ALTER Table meta_element
      ADD Constraint chk_m_elmt_typ CHECK (typ = 'F' or typ = 'H' or typ = 'D')
;

--creation sequence pour index auto-incrementation
CREATE SEQUENCE seq_elmt;
select seq_elmt.nextval from dual;

create or replace trigger t_elmt_seq
before insert on meta_element for each row
begin
   select seq_elmt.nextval into :new.id from dual;
end t_elmt_seq;
/


create   table meta_star (
       idf number(5),
       idd number (5)
);
ALTER Table meta_star
      ADD Constraint pk_m_star PRIMARY KEY (idf, idd)
;

create   table meta_hierarchy(
       idd number(5),
       idh number (5)
);

ALTER Table meta_hierarchy
      ADD Constraint pk_m_hie PRIMARY KEY (idd, idh)
;

create   table meta_attribute(
       ida number (5),
       name varchar2 (30),
       dom number (1),     -- comme en java, 0 date, 1 varchar, 2 number
       len number (2),
       pre number (2)
);

ALTER Table meta_attribute
      ADD Constraint pk_m_att PRIMARY KEY (ida)
;

--creation sequence pour index auto-incrementation
CREATE SEQUENCE seq_att;
select seq_att.nextval from dual;

create or replace trigger t_att_seq
before insert on meta_attribute for each row
begin
   select seq_att.nextval into :new.ida from dual;
end t_elmt_seq;
/

create   table meta_measure(
       idf number(5),        -- idf -->id
       idm number (5)         --idm -->ida
);
ALTER Table meta_measure
      ADD Constraint pk_m_meas PRIMARY KEY (idf, idm)
;
create   table meta_level(
       idh number(5),
       idp number (5),
       pos  number(1),                  -- je sai pas ce que c
       typ varchar(1)                  --typ  = P, W
);

ALTER Table meta_level
      ADD Constraint pk_m_lev PRIMARY KEY (idh, idp);
ALTER Table meta_level
      ADD Constraint chk_m_lev_typ CHECK (typ = 'P' or typ = 'W');

desc meta_element;
desc meta_star;
desc meta_hierarchy;
desc meta_attribute;
desc meta_measure;
desc meta_level;
------------------------
-- CREATION VUES
------------------------

--USER_FACT
--CREATE   VIEW user_fact (user_name)
--AS (select name from meta_element where typ = 'F');

   --USER_DIMENSION
--CREATE   VIEW user_dimension (dimension_name)
--AS (select name from meta_element where typ = 'D');

   --USER_STAR
--CREATE   VIEW user_star (fact_name, dimension_name)
--AS (select f.name, d.name from meta_element f, meta_element d, meta_star s
--           where f.typ = 'F' and d.typ='D' and f.id= s.idf and d.id = s.idd);

   --USER_HIERARCHY
--CREATE   VIEW user_hierarchy (dimension_name, hierarchy_name)
--AS (select d.name, h.name from meta_element d, meta_element h, meta_hierarchy s
--           where d.typ = 'D' and h.typ='H' and d.id= s.idd and h.id = s.idh);

   --USER_MEASURE
--CREATE   VIEW user_measure (fact_name, measure_name, domain_, length, precision)
--AS (select f.name, m.name, m.dom, m.len, m.pre
--           from meta_element f, meta_attribute m, meta_measure s
 --          where f.typ = 'F' and f.id= s.idf and m.ida = s.idm);
           
           --USER_PARAMETER       typ= P?
--CREATE   VIEW user_parameter (dimension_name, parameter_name, domain_, length, precision)
--AS (select d.name, p.name, p.dom, p.len, p.pre
--           from meta_element d, meta_attribute p, meta_hierarchy mh, meta_level ml
--           where d.typ = 'D' and d.id= mh.idd and mh.idh = ml.idh and ml.idp = p.ida);



           --USER_LEVEL            typ = P?
--CREATE   VIEW user_level (dimension_name, hierarchy_name, parameter_name, position_)
--AS (select d.name, h.name, p.name, ml.pos
 --          from meta_element d, meta_element h, meta_hierarchy mh, meta_level ml, meta_attribute p
--           where d.typ = 'D' and h.typ = 'H'
--           and d.id = mh.idd and h.id = mh.id and mh.idh = ml.idh and ml.idp = p.ida);

           --USER_WEAK_ATTRIBUTE
--AS (select d.name, h.name, p.name, ml.pos
--           from meta_element d, meta_element h, meta_hierarchy mh, meta_level ml, meta_attribute p
--           where d.typ = 'D' and h.typ = 'H'
 --          and d.id = mh.idd and h.id = mh.id and mh.idh = ml.idh and ml.idp = p.ida);







