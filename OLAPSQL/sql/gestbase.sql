-------------------------------------------------------------
--PACKAGE GEST_BASE_3D
--Specification
-------------------------------------------------------------
CREATE OR REPLACE PACKAGE GEST_BASE_3D
AS

          ---------------------------
          -- Commande DROP
          ---------------------------
          --DROP un fait  ok
	       PROCEDURE DROP_FACT (nom meta_element.name%type);
           --DROP une dim     ok
           PROCEDURE DROP_DIM (nomtable meta_element.name%type);
	          
               
                      
           --delete l'attribut nomatt ds table nomtable
           -- delete att dans meta_attribute
           -- delete liaison att-table ds meta measure
           --ok
           FUNCTION DELETE_ATT (idElmt meta_element.id%type,
                               nomatt meta_attribute.name%type)return  meta_attribute.ida%type;


           --delete la hierarchy de l'idh
           -- delete liaisons hierarchy-levels dans meta_level : idp, souscat, cat en 'P' libelle en 'W'
           -- delete liaison iddimension, iddhiera dans meta_hierarchy
           -- delete hirarchy dans meta_elmt 'H'
		   --ok
           PROCEDURE DELETE_HIERARCHY (num meta_element.id%type);

           ---------------------------
           -- Commande CREATE
           -- par java : creation table
           -- insertion fait/dim dans meta_element
           ---------------------------
           --cree un element dans meta_element et retourne son id
	       --ok
           FUNCTION CREATE_ELEMENT (nom meta_element.name%type,
                            typ meta_element.typ%type) return meta_element.id%type;
                            
           --ajoute un attribut a un fait/dom, retourne son id
           --insertion 1 attribut nomatt dans meta_attribute
           --liaison ds meta_measure avec table
	       --ok
           FUNCTION CREATE_ATT (id  meta_element.id%type,
                                 nomatt meta_attribute.name%type,
                                 doma meta_attribute.dom%type,     -- comme en java, 0 date, 1 varchar, 2 number
                                 p_long meta_attribute.len%type,
                                 prec meta_attribute.pre%type) RETURN meta_attribute.ida%type;

           -- connecte un fait id a une dim idd  pour creation fait
           --ok
           PROCEDURE CONNECT_DIM (idf  meta_element.id%type,
                                   idde meta_element.id%type);
           
           --cree un element H et le relie a la dim idd
           --ok
           FUNCTION CREATE_HIERARCHY (idd meta_element.id%type,
                           nomH meta_element.name%type) return meta_element.id%type;
           
           
           --relie une hierarchy a un level
           --ok
           PROCEDURE ADD_LEVEL (idh  meta_element.id%type,
                      idl meta_attribute.ida%type,
                      posit meta_level.pos%type,
                      tip meta_level.typ%type);
           
           ---------------------------
           -- Commande ALTER
           --java : ajout/delete column
           ---------------------------
           --ok
           PROCEDURE ALT_FACT_CONNECT (nomfait meta_element.name%type,
                             nomdim meta_element.name%type);
           
           --ok
           PROCEDURE ALT_FACT_DISCONNECT (nomfait meta_element.name%type,
                             nomdim meta_element.name%type);
  
  
                             --PROCEDURE ALT_DIM_DROP_H (nomh meta_element.name%type);
           ---------------------------
           -- UTILS
           ---------------------------
           --retourne l id d un attribut dun fait/dimnom
           --ok
           FUNCTION GET_ID_ATT(ide meta_element.id%type,
                      nomatt meta_attribute.name%type)
                      return meta_attribute.ida%type;
           
           --retourne l id d un elmt
           --ok
           FUNCTION GET_ID_ELMT(nom meta_element.name%type,
                      t meta_element.typ%type)
                      return meta_element.id%type;
END GEST_BASE_3D;
/







-------------------------------------------------------------
--PACKAGE GEST_BASE_3D
--Body
-------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY GEST_BASE_3D 
AS

           -------------------------------------
           --  DROP_FACT
		--delete liaisons ds meta star
		-- delete elmt
		--ok
           -------------------------------------
		PROCEDURE DROP_FACT (nom meta_element.name%type)

	    AS
              idff meta_element.id%type;
              idatt meta_attribute.ida%type;
        BEGIN
             idff := GET_ID_ELMT(nom,'F');
			 if (idff <> -1) then
                  --delete ses attributs marche pas...
                  for ligne in (SELECT m.idm FROM meta_measure m WHERE m.idf = idff )  
                  LOOP
			          delete from  meta_measure WHERE idm = ligne.idm and idf = idff;
            	      delete from  meta_attribute WHERE ida = ligne.idm;
                  END LOOP;
                  --delete dimensions
                   --for ligne in (SELECT mh.name FROM meta_star m, meta_element mh WHERE m.idf = idff and mh.id=m.idd)
                  --LOOP
                             --TODO penser a supprimer les tables des dims
                  --           DROP_DIM (ligne.name);
                  --END LOOP;

                  delete from meta_star where idf =idff ;
		 	      delete from meta_element m WHERE m.id =idff;
		    else
			      DBMS_OUTPUT.PUT_LINE('DROP_FACT : FACT DOESNT EXIST');
            end if ;
		EXCEPTION
			WHEN OTHERS THEN
			     DBMS_OUTPUT.PUT_LINE('erreur delete');
			     DBMS_OUTPUT.PUT_LINE(sqlerrm);
  	    END  DROP_FACT;



           -------------------------------------
           --  DROP_DIM 
	       --DROP une dim
           -------------------------------------
	       PROCEDURE DROP_DIM (nomtable meta_element.name%type)
           AS
              idim meta_element.id%type;
              idh meta_element.id%type;
              nomcol varchar2(30);
           BEGIN
            idim := GET_ID_ELMT(nomtable,'D');
			if (idim <> -1) then

                  --delete ses attributs
                  for ligne in (SELECT m.idm FROM meta_measure m WHERE m.idf = idim )
                  LOOP
			          delete from  meta_measure WHERE idm = ligne.idm and idf = idim;
            	      delete from  meta_attribute WHERE ida = ligne.idm;
                   END LOOP;
			       --delete ses hierarchys
                   for ligne in (SELECT m.idh FROM meta_hierarchy m WHERE m.idd = idim )
                   LOOP
			           DELETE_HIERARCHY(ligne.idh);
                   END LOOP;

                   --delete ses liens dans les faits
                    for ligne in (SELECT m.name FROM meta_element m, meta_star ms WHERE ms.idd = idim and m.id = ms.idf )
                   LOOP
                         ALT_FACT_DISCONNECT(ligne.name, nomtable);
                   END LOOP;


                   --delete dimension
			       delete from meta_element m WHERE m.id =idim;
             else
  			       DBMS_OUTPUT.PUT_LINE('DROP_DIM : DIM DOESNT EXIST');
            end if ;
		EXCEPTION
			WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('erreur delete');
			DBMS_OUTPUT.PUT_LINE(sqlerrm);
  	    END  DROP_DIM;
           


           
           -------------------------------------
           --  DELETE_ATT
           --delete l'attribut nomatt ds table nomtable
                    -- delete att dans meta_attribute
                    -- delete liaison att-table ds meta measure
           -------------------------------------
           --ok
           FUNCTION DELETE_ATT (idElmt meta_element.id%type,
                               nomatt meta_attribute.name%type) return  meta_attribute.ida%type
           AS
              idatt meta_attribute.ida%type;
           BEGIN
                  idatt := GET_ID_ATT(idElmt, nomatt);
                  if (idatt <> -1) then
                     delete from meta_measure where idf =idElmt and idm = idatt;
                     delete from meta_attribute where ida =idatt ;
                  end if ;
                  return idatt;
           END  DELETE_ATT;





           -------------------------------------
           --  DELETE_HIERARCHY
           --delete la hierarchy de l'idh
                       -- delete liaisons hierarchy-levels dans meta_level : idp, souscat, cat en 'P' libelle en 'W'
                       -- delete liaison iddimension, iddhiera dans meta_hierarchy
                       -- delete hirarchy dans meta_elmt 'H'
           --TODO a retester
           -------------------------------------
           --ok
           PROCEDURE DELETE_HIERARCHY (num meta_element.id%type)
	       AS
        	   	--num meta_element.id%type;
  	       BEGIN
                --num := GET_ID_ELMT(nom,'F');
			  if (num <> -1) then
            	--select id into num from meta_element where id=idh and typ='H';
            	delete from meta_level where meta_level.idh = num;
            	delete from meta_hierarchy where meta_hierarchy.idh = num;
            	delete from meta_element where id=num;
             end if;

	       EXCEPTION
            	WHEN NO_DATA_FOUND THEN
            		DBMS_OUTPUT.PUT_LINE('DELETE_HIERARCHY : HIERARCHY DONT EXIST');
           END DELETE_HIERARCHY;


		-------------------------------------
          	--  CREATE_ELEMENT
		--cree un element dans meta_element et retourne son id
           	--------------------------------------
	        FUNCTION CREATE_ELEMENT (nom meta_element.name%type,
                            typ meta_element.typ%type) return meta_element.id%type

            AS
              ind meta_element.id%type;
            BEGIN
                 insert into meta_element VALUES (NULL, nom,typ);
        	 select seq_elmt.currval into ind from dual;
                  return ind;
            EXCEPTION
                WHEN TOO_MANY_ROWS  then
                     DBMS_OUTPUT.PUT_LINE('CREATE_ELEMENT : DATA ALREADY EXIST');
			return -1;
            END CREATE_ELEMENT;



           -------------------------------------
           --  CREATE_ATT
           --ajoute un attribut a un fait/dom, retourne son id
           --insertion 1 attribut nomatt dans meta_attribute
           --liaison ds meta_measure avec table
           --------------------------------------
            FUNCTION CREATE_ATT (id  meta_element.id%type,
                                 nomatt meta_attribute.name%type,
                                 doma meta_attribute.dom%type,     -- comme en java, 0 date, 1 varchar, 2 number
                                 p_long meta_attribute.len%type,
                                 prec meta_attribute.pre%type) RETURN meta_attribute.ida%type
            AS
              ind meta_attribute.ida%type;
            BEGIN
                  --insert into meta_attribute VALUES (1, nomatt, doma, long, prec);
                  insert into meta_attribute VALUES (NULL, nomatt,doma,p_long,prec);
        	        select seq_att.currval into ind from dual;
                  insert into meta_measure values (id, ind);
                  return ind;
            EXCEPTION
                WHEN OTHERS  then
                     DBMS_OUTPUT.PUT_LINE('GET_ID_ELMT : NO DATA FOUND');
                     return 0;
            END CREATE_ATT;
          

          
            -------------------------------------
            --  CONNECT_DIM
            -- connecte un fait id a une dim idd  pour creation fait
            -------------------------------------
            PROCEDURE CONNECT_DIM (idf  meta_element.id%type,
                                   idde meta_element.id%type)
        	IS
                 num meta_element.id%type;
        	BEGIN
        	     select id into num from meta_element where id=idde and typ='D';
        	       select id into num from meta_element where id=idf and typ='F';
                	insert into meta_star VALUES (idf,idde);

            EXCEPTION
  	            WHEN NO_DATA_FOUND THEN
        		     DBMS_OUTPUT.PUT_LINE('CONNECT_DIM : ERROR CONNECTION');
        	END CONNECT_DIM;
        	

          
            -------------------------------------
            --  CREATE_HIERARCHY
            --cree un element H et le relie a la dim idd
            -------------------------------------
          	FUNCTION CREATE_HIERARCHY (idd meta_element.id%type,
                                      nomH meta_element.name%type) 
                 return meta_element.id%type
        	IS
        	     num meta_element.id%type;
        	BEGIN
                 select id into num from meta_element where id=idd and typ='D';
        		 insert into meta_element VALUES (null, nomH,'H');
        		 select seq_elmt.currval into num from dual;
        		 insert into meta_hierarchy VALUES(idd, num);
        		 return num;
        	EXCEPTION
                 WHEN NO_DATA_FOUND THEN
        		      DBMS_OUTPUT.PUT_LINE('CREATE_HIERARCHY : DIMENSION NOT EXIST');
        		      return 0;
        	END CREATE_HIERARCHY;



            -------------------------------------
            --  ADD_LEVEL
	  	    --relie une hierarchy a un level
	  	    -------------------------------------
	       --ok
          	PROCEDURE ADD_LEVEL (idh  meta_element.id%type,
                              idl meta_attribute.ida%type,
                              posit meta_level.pos%type,
                              tip meta_level.typ%type)
        	IS
                 num meta_element.id%type;
        	     numa meta_attribute.ida%type;
        	BEGIN
            	select id into num from meta_element where id=idh and typ='H';
            	select ida into numa from meta_attribute where ida=idl;
            	insert into meta_level VALUES (idh,idl, posit, tip);
        	EXCEPTION
                WHEN NO_DATA_FOUND THEN
        		     DBMS_OUTPUT.PUT_LINE('ADD_LEVEL : HIERARCHY OR LEVEL NOT EXIST');
        	END ADD_LEVEL;



            -------------------------------------
            --  ALT_FACT_CONNECT
            -------------------------------------
            --ok
            PROCEDURE ALT_FACT_CONNECT (nomfait meta_element.name%type,
                             nomdim meta_element.name%type)
        	IS
        	     numf meta_element.id%type;
        	     numd meta_element.id%type;
        	BEGIN
            	select id into numf from meta_element where name=nomfait and typ='F';
            	select id into numd from meta_element where name=nomdim and typ='D';
            	insert into meta_star VALUES (numf,numd);
        	EXCEPTION
             	WHEN NO_DATA_FOUND THEN
            		DBMS_OUTPUT.PUT_LINE('ALT_FACT_CONNECT : FAIT OR DIM DONT EXIST');
            	WHEN TOO_MANY_ROWS THEN
            		DBMS_OUTPUT.PUT_LINE('ALT_FACT_CONNECT : TOO MANY ROWS');
        	END ALT_FACT_CONNECT;
	
	

            -------------------------------------
            --  ALT_FACT_DISCONNECT
            -------------------------------------
		    --ok
	        PROCEDURE ALT_FACT_DISCONNECT (nomfait meta_element.name%type,
                             nomdim meta_element.name%type)
        	IS
            	numf meta_element.id%type;
            	numd meta_element.id%type;
        	BEGIN
            	select id into numf from meta_element where name=nomfait and typ='F';
            	select id into numd from meta_element where name=nomdim and typ='D';
            	delete from meta_star WHERE idf= numf and idd= numd;
        	EXCEPTION
            	WHEN NO_DATA_FOUND THEN
            		DBMS_OUTPUT.PUT_LINE('ALT_FACT_DISCONNECT : FAIT OR DIM DONT EXIST');
            	WHEN TOO_MANY_ROWS THEN
            		DBMS_OUTPUT.PUT_LINE('ALT_FACT_DISCONNECT : TOO MANY ROWS');
        	END ALT_FACT_DISCONNECT;
    
            -------------------------------------
            --  GET_ID_ATT
            --retourne l id d un attribut dun fait/dim
            -------------------------------------
	        --ok
        	FUNCTION GET_ID_ATT(ide meta_element.id%type,
        				nomatt meta_attribute.name%type)
          				return meta_attribute.ida%type
          	IS
          	     numa meta_attribute.ida%type;
          	BEGIN
     	        select matt.ida into numa
          		       from meta_attribute matt, meta_measure mm
          		       where matt.name = nomatt and matt.ida = mm.idm
          		             and mm.idf = ide;
                 return numa;
          	EXCEPTION
            	WHEN NO_DATA_FOUND THEN
            		DBMS_OUTPUT.PUT_LINE('GET_ID_ATT : ELMT OR ATTRIBUTE DONT EXIST');
            		return -1;
            	WHEN TOO_MANY_ROWS THEN
            		DBMS_OUTPUT.PUT_LINE('GET_ID_ATT : TOO MANY ROWS');
            		return -1;
          	END GET_ID_ATT;
          	
          	

            -------------------------------------
            --  GET_ID_ELMT
            --retourne l id d un elmt
            -------------------------------------
		--ok
            FUNCTION GET_ID_ELMT(nom meta_element.name%type,
                                   t meta_element.typ%type)
                                   return meta_element.id%type
         	AS
                 num meta_element.id%type;
        	BEGIN
        	     select id into num from meta_element
        		        where name = nom and typ = t;
                 return num;
        	EXCEPTION
        	WHEN NO_DATA_FOUND THEN
        		DBMS_OUTPUT.PUT_LINE('GET_ID_ELMT : ELMT OR ATTRIBUTE DONT EXIST');
        		return -1;
        	WHEN TOO_MANY_ROWS THEN
        		DBMS_OUTPUT.PUT_LINE('GET_ID_ELMT : TOO MANY ROWS');
        		return -1;
        	END GET_ID_ELMT;

END GEST_BASE_3D;
/
show error

