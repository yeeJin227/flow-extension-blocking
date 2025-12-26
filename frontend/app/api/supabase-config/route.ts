import { NextResponse } from 'next/server';

export async function GET() {
  // 환경변수에서 Supabase 설정 가져오기
  const config = {
    url: process.env.NEXT_PUBLIC_SUPABASE_URL || '',
    key: process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY || '',
    fixedTable: process.env.NEXT_PUBLIC_SUPABASE_FIXED_TABLE || 'fixed_extension',
    customTable: process.env.NEXT_PUBLIC_SUPABASE_CUSTOM_TABLE || 'custom_extension',
  };

  // 환경변수가 설정되어 있는지 확인
  const isConfigured = !!(config.url && config.key);

  return NextResponse.json({
    ...config,
    isConfigured,
  });
}

