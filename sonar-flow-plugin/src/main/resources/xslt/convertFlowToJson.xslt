<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	
    <!-- Turn off auto-insertion of <?xml> tag and set indenting on -->
    <xsl:output method="text" encoding="utf-8" indent="yes"/>
    
    <!-- strip whitespace from whitespace-only nodes -->
    <xsl:strip-space elements="*"/>

	<!-- start with the root element -->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="FLOW">
		<xsl:text>{"flow":{"children":[</xsl:text>
			<xsl:call-template name="runChildren"/>
		<xsl:text>]}}</xsl:text>
	</xsl:template>
	
	<xsl:template match="SEQUENCE">
		<xsl:param name="pos"/>
		<xsl:if test="$pos > 1"><xsl:text>,</xsl:text></xsl:if>
		<xsl:text>{"sequence":{"label":"</xsl:text><xsl:value-of select="@NAME"/><xsl:text>","comment":"</xsl:text><xsl:value-of select="COMMENT/text()"/><xsl:text>","line":"</xsl:text><xsl:value-of select="@location"/><xsl:text>","children":[</xsl:text>
			<xsl:call-template name="runChildren"/>
		<xsl:text>]}}</xsl:text>
	</xsl:template>
	
	<xsl:template match="MAP">	
		<xsl:param name="pos"/>
		<xsl:if test="$pos > 1"><xsl:text>,</xsl:text></xsl:if>
		<xsl:text>{"map":{"label":"</xsl:text><xsl:value-of select="@NAME"/><xsl:text>","comment":"</xsl:text><xsl:value-of select="COMMENT/text()"/><xsl:text>","line":"</xsl:text><xsl:value-of select="@location"/><xsl:text>"}}</xsl:text>
	</xsl:template>
	
	<xsl:template match="INVOKE">
		<xsl:param name="pos"/>
		<xsl:if test="$pos > 1"><xsl:text>,</xsl:text></xsl:if>
		<xsl:text>{"invoke":{"service":"</xsl:text><xsl:value-of select="@SERVICE"/><xsl:text>","label":"</xsl:text><xsl:value-of select="@NAME"/><xsl:text>","comment":"</xsl:text><xsl:value-of select="COMMENT/text()"/><xsl:text>","line":"</xsl:text><xsl:value-of select="@location"/><xsl:text>"}}</xsl:text>
	</xsl:template>
	
	<xsl:template match="EXIT">
		<xsl:param name="pos"/>
		<xsl:if test="$pos > 1"><xsl:text>,</xsl:text></xsl:if>
		<xsl:text>{"exit":{"label":"</xsl:text><xsl:value-of select="@NAME"/><xsl:text>","comment":"</xsl:text><xsl:value-of select="COMMENT/text()"/><xsl:text>","line":"</xsl:text><xsl:value-of select="@location"/><xsl:text>"}}</xsl:text>
	</xsl:template>
	
	<xsl:template match="LOOP">
		<xsl:param name="pos"/>
		<xsl:if test="$pos > 1"><xsl:text>,</xsl:text></xsl:if>
		<xsl:text>{"loop":{"label":"</xsl:text><xsl:value-of select="@NAME"/><xsl:text>","comment":"</xsl:text><xsl:value-of select="COMMENT/text()"/><xsl:text>","line":"</xsl:text><xsl:value-of select="@location"/><xsl:text>","children":[</xsl:text>
			<xsl:call-template name="runChildren"/>
		<xsl:text>]}}</xsl:text>
	</xsl:template>
	
	<xsl:template match="REPEAT">
		<xsl:param name="pos"/>
		<xsl:if test="$pos > 1"><xsl:text>,</xsl:text></xsl:if>
		<xsl:text>{"loop":{"label":"</xsl:text><xsl:value-of select="@NAME"/><xsl:text>","comment":"</xsl:text><xsl:value-of select="COMMENT/text()"/><xsl:text>","line":"</xsl:text><xsl:value-of select="@location"/><xsl:text>","children":[</xsl:text>
			<xsl:call-template name="runChildren"/>
		<xsl:text>]}}</xsl:text>
	</xsl:template>
		
	<xsl:template match="BRANCH">
		<xsl:param name="pos"/>
		<xsl:if test="$pos > 1"><xsl:text>,</xsl:text></xsl:if>
		<xsl:text>{"branch":{"switch":"</xsl:text><xsl:value-of select="@SWITCH"/><xsl:text>","label":"</xsl:text><xsl:value-of select="@NAME"/><xsl:text>","comment":"</xsl:text><xsl:value-of select="COMMENT/text()"/><xsl:text>","line":"</xsl:text><xsl:value-of select="@location"/><xsl:text>","children":[</xsl:text>
			<xsl:call-template name="runChildren"/>
		<xsl:text>]}}</xsl:text>	
	</xsl:template>
	
	<xsl:template name="runChildren">
		<xsl:apply-templates select="*[contains('SEQUENCE,MAP,INVOKE,EXIT,LOOP,REPEAT,BRANCH', name())][1]"><xsl:with-param name="pos" select="1"/></xsl:apply-templates>
		<xsl:apply-templates select="*[contains('SEQUENCE,MAP,INVOKE,EXIT,LOOP,REPEAT,BRANCH', name())][position()>1]"><xsl:with-param name="pos" select="2"/></xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="@* | node()">
	</xsl:template>
	
</xsl:stylesheet>