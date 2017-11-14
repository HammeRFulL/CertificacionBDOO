-- ================================================================ --
-- Author:		<Carlos Herrera Verdugo>		    --
-- Proyecto:		<Certificación BDOO>			    --
-- Create date: 	<31-10-2017>				    --
-- Description:		<Procedimientos Certificación BDOO>	    --
-- Profesor:		<Claudio Diaz Pacheco>			    --
-- ================================================================ --

----------------------------------------------------------------------------------------------
-----------------------------------------CRUD CLIENTE-----------------------------------------
----------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE CLIENTE_CRUD(OPCION NUMBER, R_INS VARCHAR, N_INS VARCHAR, 
A_INS VARCHAR, T_INS VARCHAR) AS
BEGIN
	IF OPCION=1 THEN
		INSERT INTO CLIENTE VALUES(UPPER(R_INS),UPPER(N_INS),
                UPPER(A_INS),UPPER(T_INS),1);
	ELSIF OPCION=2 THEN
		UPDATE CLIENTE SET NOMBRE=UPPER(N_INS),APELLIDO=UPPER(A_INS), 
                TELEFONO=UPPER(T_INS), ESTADO=1 
		WHERE RUT=UPPER(R_INS);
	ELSIF OPCION=3 THEN
		UPDATE CLIENTE SET ESTADO=0 WHERE RUT=UPPER(R_INS);
	END IF;
END;
/

----------------------------------------------------------------------------------------------
-------------------------------------------CRUD JEFE------------------------------------------
----------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE JEFE_CRUD(OPCION NUMBER, R_INS VARCHAR, N_INS VARCHAR, 
A_INS VARCHAR, T_INS VARCHAR, ND_INS VARCHAR, S_INS NUMBER) AS
	BEGIN
	IF OPCION=1 THEN
		INSERT INTO JEFEDEPTO VALUES(UPPER(R_INS),UPPER(N_INS),UPPER(A_INS),
                UPPER(T_INS),1,UPPER(ND_INS),S_INS);
	ELSIF OPCION=2 THEN
		UPDATE JEFEDEPTO SET NOMBRE=UPPER(N_INS),APELLIDO=UPPER(A_INS), 
                TELEFONO=UPPER(T_INS), NOMBREDEPTO=UPPER(ND_INS), SUELDO=S_INS, ESTADO=1 
		WHERE RUT=R_INS;
	ELSIF OPCION=3 THEN
		UPDATE JEFEDEPTO SET ESTADO=0 WHERE RUT=UPPER(R_INS);
	END IF;
	END;
/
----------------------------------------------------------------------------------------------
----------------------------------------CRUD VENDEDOR-----------------------------------------
----------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE VENDEDOR_CRUD(OPCION NUMBER, R_INS VARCHAR, N_INS VARCHAR, 
A_INS VARCHAR, T_INS VARCHAR, FI_INS DATE, SB_INS NUMBER, JEFE_RUT VARCHAR) AS
BEGIN
	IF OPCION=1 THEN
		INSERT INTO VENDEDOR SELECT UPPER(R_INS),UPPER(N_INS),UPPER(A_INS),
                UPPER(T_INS),1,FI_INS,SB_INS,REF(JD) 
		FROM JEFEDEPTO JD WHERE JD.RUT=UPPER(JEFE_RUT);	
	ELSIF OPCION=2 THEN
		UPDATE VENDEDOR SET NOMBRE=UPPER(N_INS),APELLIDO=UPPER(A_INS), 
                TELEFONO=UPPER(T_INS), FECHA_INGRESO=FI_INS, SUELDO_BASE=SB_INS, ESTADO=1, 
		JEFE = (SELECT REF(JD) FROM JEFEDEPTO JD WHERE JD.RUT=UPPER(JEFE_RUT)) 
		WHERE RUT=UPPER(R_INS);
	ELSIF OPCION=3 THEN
		UPDATE VENDEDOR SET ESTADO=0 WHERE RUT=UPPER(R_INS);
	END IF;
END;
/

----------------------------------------------------------------------------------------------
-----------------------------------------CRUD DETALLE-----------------------------------------
----------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE DETALLE_CRUD(F_INS NUMBER, ITEM_INS NUMBER,
CANT_INS NUMBER, PV_INS NUMBER) AS
BEGIN
	INSERT INTO DETALLEFACTURA SELECT REF(F),REF(P),CANT_INS,PV_INS
	FROM FACTURA F, PRODUCTO P
	WHERE F.NUMERO=F_INS AND P.CODIGO=ITEM_INS;
END;
/

----------------------------------------------------------------------------------------------
-----------------------------------------CRUD FACTURA-----------------------------------------
----------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE FACTURA_CRUD(N_INS NUMBER, F_INS DATE, 
RUT_CLIENTE VARCHAR, RUT_VENDEDOR VARCHAR) AS
BEGIN
	INSERT INTO FACTURA SELECT N_INS,F_INS,REF(C),REF(V) 
	FROM VENDEDOR V, CLIENTE C 
	WHERE C.RUT=UPPER(RUT_CLIENTE) AND V.RUT=UPPER(RUT_VENDEDOR);	
END;
/
----------------------------------------------------------------------------------------------
----------------------------------------CRUD PRODUCTO-----------------------------------------
----------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE PRODUCTO_CRUD(OPCION NUMBER, C_INS NUMBER, D_INS VARCHAR, 
P_INS NUMBER, S_INS NUMBER, SC_INS NUMBER) AS
	BEGIN
	IF OPCION=1 THEN
		INSERT INTO PRODUCTO VALUES(C_INS,UPPER(D_INS),P_INS,S_INS,SC_INS,1);
	ELSIF OPCION=2 THEN
		UPDATE PRODUCTO SET DESCRIPCION=UPPER(D_INS),PRECIO=P_INS, STOCK=S_INS, 
		STOCK_CRITICO=SC_INS, ESTADO=1 
		WHERE CODIGO=C_INS;
	ELSIF OPCION=3 THEN
		UPDATE PRODUCTO SET ESTADO=0 WHERE CODIGO=C_INS;
	END IF;
	END;
/