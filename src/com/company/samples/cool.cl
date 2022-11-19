class Main inherits IO {
    char: String;
    main() : SELF_TYPE {
	{
        --out_string("1234".substr(0,3).substr(1,3));
	    out_string((new Object).type_name().substr(1,3)).
	    out_string((isvoid self).type_name().substr(1,3));
	    --out_string("\n");
	}
    };
};
