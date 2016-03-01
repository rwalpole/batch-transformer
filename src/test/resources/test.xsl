<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="html" version="5.0" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
    <xsl:strip-space elements="*"/>
    <xsl:param name="name" required="yes"/>
    <xsl:param name="year" required="yes"/>
    <xsl:template match="/books">
        <html>
            <head>
                <title><xsl:value-of select="concat($name,'''s Books ',$year)"/></title>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="book">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="title">
        <h1>
            <xsl:value-of select="."/>
        </h1>
    </xsl:template>
    <xsl:template match="author">
        <p><strong><xsl:value-of select="concat('Author: ', .)"/></strong></p>
    </xsl:template>
    <xsl:template match="published">
        <p><xsl:value-of select="concat('Published: ', .)"/></p>        
    </xsl:template>
    <xsl:template match="summary">
        <p><xsl:value-of select="."/></p>   
    </xsl:template>
</xsl:stylesheet>